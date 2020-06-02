package com.a6raywa1cher.muchelpspring.utils;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.CustomAuthentication;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationResolverImpl implements AuthenticationResolver {
	@Autowired
	UserService userService;

	@Override
	public User getUser() {
		CustomAuthentication customAuthentication = (CustomAuthentication) SecurityContextHolder.getContext().getAuthentication();
		return userService.getById(customAuthentication.getPrincipal()).orElseThrow();
	}
}
