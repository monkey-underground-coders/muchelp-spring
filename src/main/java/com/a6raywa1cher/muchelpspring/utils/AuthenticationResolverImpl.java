package com.a6raywa1cher.muchelpspring.utils;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.OAuthUserInfo;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResolverImpl implements AuthenticationResolver {
	private final UserService userService;

	public AuthenticationResolverImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public User getUser() {
		OAuthUserInfo oAuthUserInfo = getOAuthUserInfo();
		if (oAuthUserInfo == null) {
			return null;
		}
		return userService.getById(oAuthUserInfo.getId()).orElseThrow();
	}

	@Override
	public OAuthUserInfo getOAuthUserInfo() {
		return (OAuthUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
