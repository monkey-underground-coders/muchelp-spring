package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtTokenService;
import com.a6raywa1cher.muchelpspring.security.jwt.service.RefreshTokenService;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtTokenService jwtTokenService;
	@Autowired
	private RefreshTokenService refreshTokenService;

	private VendorId getVendorId(OAuth2AuthenticationToken authentication) {
		return VendorId.valueOf(authentication.getAuthorizedClientRegistrationId().toUpperCase());
	}

	private User getUserOrRegister(OAuth2AuthenticationToken authentication) {
		OAuth2User oAuth2User = authentication.getPrincipal();
		VendorId vendor = getVendorId(authentication);
		String email = oAuth2User.getAttribute("email");
		String id = oAuth2User.getAttribute("sub");
		Optional<User> optionalUser = userService.getByVendorIdOrEmail(vendor, id, email);
		User user;
		if (optionalUser.isEmpty()) { // if that's a new user, register him
			user = new User();
			switch (vendor) {
				case GOOGLE:
					user.setGoogleId(id);
					break;
				case VK:
					user.setVkId(id);
					break;
			}
			user.setLastVisit(LocalDateTime.now());
			user.setPicture(oAuth2User.getAttribute("picture"));
			user.setName(oAuth2User.getAttribute("name"));
			user.setEmail(email);
			user.setCreatedAt(LocalDateTime.now());
			userRepository.save(user);
		} else { // or else check email collisions
			user = optionalUser.get();
			switch (vendor) {
				case GOOGLE:
					if (user.getGoogleId() == null) {
						user.setGoogleId(id);
						userRepository.save(user);
					}
					break;
				case VK:
					if (user.getGoogleId() == null) {
						user.setVkId(id);
						userRepository.save(user);
					}
					break;
			}
		}
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		Authentication newAuthentication = authentication;
		if (authentication instanceof OAuth2AuthenticationToken) { // exchange OAuth information to JWT + refresh pair
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			OAuth2User oAuth2User = token.getPrincipal();
			User user = getUserOrRegister(token);
			JwtToken jwt = jwtTokenService.issue(getVendorId(token), oAuth2User.getAttribute("sub"), user.getId());
			CustomAuthentication customAuthentication = new CustomAuthentication(
					authentication.getAuthorities(), jwt
			);
			SecurityContextHolder.getContext().setAuthentication(customAuthentication);
			newAuthentication = customAuthentication;
			response.setHeader("MHS-JWTToken", jwt.getToken());

			RefreshToken refreshToken = refreshTokenService.issue(user);
			response.setHeader("MHS-RefreshToken", refreshToken.getToken());
		}
		super.onAuthenticationSuccess(request, response, newAuthentication);
		request.getSession().invalidate();
	}
}
