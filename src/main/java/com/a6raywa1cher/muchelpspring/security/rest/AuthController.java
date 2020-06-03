package com.a6raywa1cher.muchelpspring.security.rest;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtRefreshPair;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtTokenService;
import com.a6raywa1cher.muchelpspring.security.jwt.service.RefreshTokenService;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;
import com.a6raywa1cher.muchelpspring.security.rest.req.GetNewJwtTokenRequest;
import com.a6raywa1cher.muchelpspring.security.rest.req.InvalidateTokenRequest;
import com.a6raywa1cher.muchelpspring.security.rest.req.LinkSocialAccountsRequest;
import com.a6raywa1cher.muchelpspring.service.UserService;
import com.a6raywa1cher.muchelpspring.utils.AuthenticationResolver;
import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final RefreshTokenService refreshTokenService;

	private final JwtTokenService jwtTokenService;

	private final AuthenticationResolver authenticationResolver;

	private final JwtRefreshPairService jwtRefreshPairService;

	private final UserService userService;

	public AuthController(AuthenticationResolver authenticationResolver, RefreshTokenService refreshTokenService,
						  JwtTokenService jwtTokenService, JwtRefreshPairService jwtRefreshPairService, UserService userService) {
		this.authenticationResolver = authenticationResolver;
		this.refreshTokenService = refreshTokenService;
		this.jwtTokenService = jwtTokenService;
		this.jwtRefreshPairService = jwtRefreshPairService;
		this.userService = userService;
	}

	@GetMapping("/user")
	@JsonView(Views.Internal.class)
	@Operation(security = @SecurityRequirement(name = "jwt"))
	public ResponseEntity<User> getCurrentUser(@Parameter(hidden = true) User user) {
		return ResponseEntity.ok(user);
	}

	@GetMapping("/convert")
	@Operation(security = @SecurityRequirement(name = "jwt"))
	public ResponseEntity<JwtRefreshPair> convertToJwt(HttpServletRequest request, Authentication authentication) {
		if (!(authentication instanceof OAuth2AuthenticationToken)) {
			return ResponseEntity.badRequest().build();
		}
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		User user = authenticationResolver.getUser();
		JwtRefreshPair pair = jwtRefreshPairService.issue(user,
				VendorId.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase()),
				token.getPrincipal().getAttribute("sub"));
		SecurityContextHolder.clearContext();
		request.getSession().invalidate();
		return ResponseEntity.ok(pair);
	}

	@PostMapping("/get_access")
	public ResponseEntity<JwtRefreshPair> getNewJwtToken(@RequestBody @Valid GetNewJwtTokenRequest request) {
		Optional<RefreshToken> optional = refreshTokenService.getByToken(request.getRefreshToken());
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		refreshTokenService.invalidate(optional.get());
		User user = optional.get().getUser();
		return ResponseEntity.ok(jwtRefreshPairService.issue(user));
	}

	@DeleteMapping("/invalidate")
	@Operation(security = @SecurityRequirement(name = "jwt"))
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
	@Operation(security = @SecurityRequirement(name = "jwt"))
	public ResponseEntity<Void> invalidateAllTokens() {
		User user = authenticationResolver.getUser();
		refreshTokenService.invalidateAll(user);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/link_social")
	public ResponseEntity<JwtRefreshPair> linkSocialAccounts(@RequestBody LinkSocialAccountsRequest request) {
		Optional<User> primaryUser = refreshTokenService.getByToken(request.getPrimaryRefreshToken())
				.flatMap(rt -> Optional.of(rt.getUser()));
		Optional<JwtToken> optionalJwtToken = jwtTokenService.decode(request.getSecondaryAccessToken());
		Optional<User> secondaryUser = optionalJwtToken.flatMap(jt -> userService.getById(jt.getUid()));
		if (primaryUser.isEmpty() || secondaryUser.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		JwtToken jwtToken = optionalJwtToken.get();
		if (jwtToken.getVendorId() == null) {
			return ResponseEntity.badRequest().build();
		}
		userService.delete(secondaryUser.get());
		userService.setVendorSub(primaryUser.get(), jwtToken.getVendorId(), jwtToken.getVendorSub());
		return ResponseEntity.ok(jwtRefreshPairService.issue(primaryUser.get(), jwtToken.getVendorId(), jwtToken.getVendorSub()));
	}
}
