package com.a6raywa1cher.muchelpspring.security.rest;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtTokenService;
import com.a6raywa1cher.muchelpspring.security.jwt.service.RefreshTokenService;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;
import com.a6raywa1cher.muchelpspring.security.rest.req.GetNewJwtTokenRequest;
import com.a6raywa1cher.muchelpspring.security.rest.req.InvalidateTokenRequest;
import com.a6raywa1cher.muchelpspring.security.rest.res.GetNewJwtTokenResponse;
import com.a6raywa1cher.muchelpspring.utils.AuthenticationResolver;
import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
	private final RefreshTokenService refreshTokenService;

	private final JwtTokenService jwtTokenService;

	private final AuthenticationResolver authenticationResolver;

	public AuthController(AuthenticationResolver authenticationResolver, RefreshTokenService refreshTokenService,
						  JwtTokenService jwtTokenService) {
		this.authenticationResolver = authenticationResolver;
		this.refreshTokenService = refreshTokenService;
		this.jwtTokenService = jwtTokenService;
	}

	@GetMapping("/user")
	@JsonView(Views.Internal.class)
	public ResponseEntity<User> getCurrentUser() {
		User user = authenticationResolver.getUser();
		if (user == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return ResponseEntity.ok(user);
	}

	@GetMapping("/convert")
	public ResponseEntity<GetNewJwtTokenResponse> convertToJwt(HttpServletRequest request, Authentication authentication) {
		if (!(authentication instanceof OAuth2AuthenticationToken)) {
			return ResponseEntity.badRequest().build();
		}
		User user = authenticationResolver.getUser();
		JwtToken accessToken = jwtTokenService.issue(user.getId());
		RefreshToken refreshToken = refreshTokenService.issue(user);
		SecurityContextHolder.clearContext();
		request.getSession().invalidate();
		return ResponseEntity.ok(new GetNewJwtTokenResponse(
				refreshToken.getToken(),
				OffsetDateTime.of(refreshToken.getExpiringAt(), OffsetDateTime.now().getOffset()),
				accessToken.getToken(),
				OffsetDateTime.of(accessToken.getExpiringAt(), OffsetDateTime.now().getOffset())
		));
	}

	@PostMapping("/get_access")
	public ResponseEntity<GetNewJwtTokenResponse> getNewJwtToken(@RequestBody @Valid GetNewJwtTokenRequest request) {
		Optional<RefreshToken> optional = refreshTokenService.getByToken(request.getRefreshToken());
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		refreshTokenService.invalidate(optional.get());
		User user = optional.get().getUser();
		JwtToken accessToken = jwtTokenService.issue(user.getId());
		RefreshToken refreshToken = refreshTokenService.issue(user);
		return ResponseEntity.ok(new GetNewJwtTokenResponse(
				refreshToken.getToken(),
				OffsetDateTime.of(refreshToken.getExpiringAt(), OffsetDateTime.now().getOffset()),
				accessToken.getToken(),
				OffsetDateTime.of(accessToken.getExpiringAt(), OffsetDateTime.now().getOffset())
		));
	}

	@DeleteMapping("/invalidate")
	public ResponseEntity<Void> invalidateToken(@RequestBody @Valid InvalidateTokenRequest request) {
		User user = authenticationResolver.getUser();
		Optional<RefreshToken> optional = refreshTokenService.getByToken(request.getRefreshToken());
		if (optional.isPresent()) {
			RefreshToken refreshToken = optional.get();
			if (user.equals(refreshToken.getUser())) {
				refreshTokenService.invalidate(refreshToken);
			}
		}
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/invalidate_all")
	public ResponseEntity<Void> invalidateAllTokens() {
		User user = authenticationResolver.getUser();
		refreshTokenService.invalidateAll(user);
		return ResponseEntity.ok().build();
	}

//	@PostMapping("/link_vk")
//	public String linkVkUser(HttpSession httpSession) {
//		User user = authenticationResolver.getUser();
//		httpSession.setAttribute("link_user_id", user.getId());
//		return "redirect:/oauth2/authorization/vk";
//	}
}
