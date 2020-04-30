package com.a6raywa1cher.muchelpspring.security;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthUserInfo extends OAuth2User {
	String getVendor();

	Long getId();

	String getVendorId();
}
