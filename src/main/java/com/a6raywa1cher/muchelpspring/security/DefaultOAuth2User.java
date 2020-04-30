package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

public class DefaultOAuth2User implements OAuthUserInfo {
	private final Map<String, Object> attributes;
	private final Collection<? extends GrantedAuthority> authorities;
	private final User user;

	public DefaultOAuth2User(User user, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
		this.attributes = attributes;
		this.authorities = authorities;
		this.user = user;
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
		return user.getEmail();
	}

	@Override
	public Long getId() {
		return user.getId();
	}

	@Override
	public String getName() {
		return user.getEmail();
	}
}
