package com.a6raywa1cher.muchelpspring.security.jwt;

import com.a6raywa1cher.muchelpspring.security.CustomAuthentication;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtTokenService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	private final JwtTokenService jwtTokenService;
	private final CustomAuthenticationProvider customAuthenticationProvider;
	private static final String AUTHORIZATION_HEADER = "Authorization";

	public JWTAuthorizationFilter(JwtTokenService jwtTokenService, CustomAuthenticationProvider customAuthenticationProvider) {
		this.jwtTokenService = jwtTokenService;
		this.customAuthenticationProvider = customAuthenticationProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (request.getHeader(AUTHORIZATION_HEADER) != null) {
			String lowerCase = request.getHeader(AUTHORIZATION_HEADER).toLowerCase();
			String token;
			if (lowerCase.startsWith("jwt ") || lowerCase.startsWith("bearer ")) {
				token = request.getHeader(AUTHORIZATION_HEADER).split(" ")[1];
			} else {
				filterChain.doFilter(request, response);
				return;
			}
			Optional<JwtToken> jwtBody = jwtTokenService.decode(token);
			if (jwtBody.isEmpty()) {
				logger.warn("Broken JWT or unknown sigh key");
			} else {
				CustomAuthentication customAuthentication = new CustomAuthentication(
						Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
						jwtBody.get()
				);
				try {
					customAuthenticationProvider.authenticate(customAuthentication);
					SecurityContextHolder.createEmptyContext();
					SecurityContextHolder.getContext().setAuthentication(customAuthentication);
				} catch (AuthenticationException e) {
					SecurityContextHolder.clearContext();
					throw e;
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}