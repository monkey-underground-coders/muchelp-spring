package com.a6raywa1cher.muchelpspring.utils;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.security.CustomAuthentication;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResolverImpl implements AuthenticationResolver {
	@Autowired
	UserService userService;

	@Override
	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof CustomAuthentication) {
			CustomAuthentication customAuthentication = (CustomAuthentication) authentication;
			return userService.getById(customAuthentication.getPrincipal()).orElseThrow();
		} else if (authentication instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
			VendorId vendorId = VendorId.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());
			String id = token.getPrincipal().getAttribute("sub");
			String email = token.getPrincipal().getAttribute("email");
			return userService.getByVendorIdOrEmail(vendorId, id, email).orElseThrow();
		}
		throw new AuthenticationResolveException("Unknown Authentication " + authentication.getClass().getCanonicalName());
	}
}
