package com.a6raywa1cher.muchelpspring.security.jwt;

import com.a6raywa1cher.muchelpspring.security.CustomAuthentication;
import com.a6raywa1cher.muchelpspring.security.jwt.service.JwtTokenService;
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

	public JWTAuthorizationFilter(JwtTokenService jwtTokenService) {
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if (request.getHeader("Authorization") != null &&
				request.getHeader("Authorization").toLowerCase().startsWith("jwt ")) {
			String token = request.getHeader("Authorization").split("jwt ")[1];
			Optional<JwtToken> jwtBody = jwtTokenService.decode(token);
			if (jwtBody.isEmpty()) {
				logger.error("Broken JWT or unknown sigh key");
			} else {
				CustomAuthentication customAuthentication = new CustomAuthentication(
						Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
						jwtBody.get()
				);
				SecurityContextHolder.createEmptyContext();
				SecurityContextHolder.getContext().setAuthentication(customAuthentication);
			}
		}
		filterChain.doFilter(request, response);
	}
}