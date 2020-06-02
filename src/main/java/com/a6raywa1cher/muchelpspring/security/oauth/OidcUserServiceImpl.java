package com.a6raywa1cher.muchelpspring.security.oauth;

import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

@Component("oidc-user-service")
public class OidcUserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {
	private final UserRepository userRepository;
	private OidcUserService oidcUserService = new OidcUserService();

	@Autowired
	public OidcUserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = oidcUserService.loadUser(userRequest);
		if (!oidcUser.getEmailVerified()) {
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT));
		}
		return oidcUser;
	}
}
