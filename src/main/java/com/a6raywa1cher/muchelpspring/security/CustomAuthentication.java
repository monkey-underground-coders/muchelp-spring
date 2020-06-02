package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthentication implements Authentication {
	private final Collection<? extends GrantedAuthority> authorities;
	private final JwtToken jwtToken;
	private boolean authenticated = true;

	public CustomAuthentication(Collection<? extends GrantedAuthority> authorities, JwtToken jwtToken) {
		this.authorities = authorities;
		this.jwtToken = jwtToken;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public JwtToken getCredentials() {
		return jwtToken;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Long getPrincipal() {
		return jwtToken.getUid();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException();
		}
		authenticated = false;
	}

	@Override
	public String getName() {
		return Long.toString(jwtToken.getUid());
	}
}
