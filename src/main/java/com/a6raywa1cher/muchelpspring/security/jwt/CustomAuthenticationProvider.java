package com.a6raywa1cher.muchelpspring.security.jwt;

import com.a6raywa1cher.muchelpspring.security.CustomAuthentication;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtTokenService;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	JwtTokenService jwtTokenService;
	@Autowired
	UserService userService;

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
