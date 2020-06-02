package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.service.UserService;
import com.a6raywa1cher.muchelpspring.utils.AuthenticationResolveException;
import com.a6raywa1cher.muchelpspring.utils.AuthenticationResolver;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;

public class LastVisitFilter extends OncePerRequestFilter {
	private final UserService service;
	private final AuthenticationResolver resolver;

	public LastVisitFilter(UserService service, AuthenticationResolver resolver) {
		this.service = service;
		this.resolver = resolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} finally {
			try {
				if (SecurityContextHolder.getContext().getAuthentication() != null) {
					User user = resolver.getUser();
					if (user.getLastVisit().plusSeconds(30).isBefore(ZonedDateTime.now()))
						service.setLastVisit(user, ZonedDateTime.now());
				}
			} catch (AuthenticationResolveException ignored) {

			} catch (Exception e) {
				logger.error("Error while setting last visit", e);
			}
		}
	}
}
