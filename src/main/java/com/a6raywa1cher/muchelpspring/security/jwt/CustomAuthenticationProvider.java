package com.a6raywa1cher.muchelpspring.security.jwt;

import com.a6raywa1cher.muchelpspring.security.CustomAuthentication;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final UserService userService;

	public CustomAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		CustomAuthentication customAuthentication = (CustomAuthentication) authentication;
		JwtToken jwtToken = customAuthentication.getCredentials();
		if (jwtToken == null) {
			customAuthentication.setAuthenticated(false);
			throw new BadCredentialsException("JwtToken not provided");
		}
		Long userId = jwtToken.getUid();
		if (userService.getById(userId).isEmpty()) {
			customAuthentication.setAuthenticated(false);
			throw new UsernameNotFoundException(String.format("User %d doesn't exists", userId));
		}
		return customAuthentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAuthentication.class.isAssignableFrom(authentication);
	}
}
