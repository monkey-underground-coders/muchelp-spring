package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

public class DefaultOidcUser extends User implements OidcUser, OAuthUserInfo {
	private OidcUser oidcUser;

	public DefaultOidcUser(User user, OidcUser oidcUser) {
		this.oidcUser = oidcUser;
		BeanUtils.copyProperties(user, this);
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oidcUser.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oidcUser.getAuthorities();
	}

	@Override
	public <A> A getAttribute(String name) {
		return oidcUser.getAttribute(name);
	}

	@Override
	public Map<String, Object> getClaims() {
		return oidcUser.getClaims();
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return oidcUser.getUserInfo();
	}

	@Override
	public OidcIdToken getIdToken() {
		return oidcUser.getIdToken();
	}

	@Override
	public String getVendor() {
		return "google";
	}

	@Override
	public String getVendorId() {
		return this.getAttribute("sub");
	}

	@Override
	public String toString() {
		return this.getEmail();
	}
}
