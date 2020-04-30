package com.a6raywa1cher.muchelpspring.utils;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.OAuthUserInfo;

public interface AuthenticationResolver {
	User getUser();

	OAuthUserInfo getOAuthUserInfo();
}
