package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class DefaultOAuth2User extends User implements OAuth2User, OAuthUserInfo {
	private final Map<String, Object> attributes;
	private final Collection<? extends GrantedAuthority> authorities;

	public DefaultOAuth2User(User user, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
		this.attributes = attributes;
		this.authorities = authorities;
		BeanUtils.copyProperties(user, this);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getVendor() {
		return "vk";
	}

	@Override
	public String getVendorId() {
		return this.getAttribute("user_id");
	}

	@Override
	public String toString() {
		return this.getEmail();
	}
}
