package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component("oidc-user-service")
public class OidcUserServiceImpl implements OAuth2UserService<OidcUserRequest, OidcUser> {
	private final UserRepository userRepository;
	private OidcUserService oidcUserService = new OidcUserService();

	@Autowired
	public OidcUserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public DefaultOidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = oidcUserService.loadUser(userRequest);
		if (!oidcUser.getEmailVerified()) {
			throw new OAuth2AuthenticationException(new OAuth2Error("Unverified email"));
		}
		String googleId = oidcUser.getSubject();
		String email = oidcUser.getEmail();
		User user = userRepository.findByGoogleIdOrEmail(googleId, email).orElseGet(() -> {
			User user1 = new User();
			user1.setGoogleId(googleId);
			user1.setCreatedAt(LocalDateTime.now());
			user1.setEmail(email);
			user1.setLastTicket(null);
			user1.setMySubjects(new ArrayList<>());
			user1.setName(oidcUser.getFullName());
			user1.setPicture(oidcUser.getPicture());
			return user1;
		});
		user.setGoogleId(googleId);
		user.setLastVisit(LocalDateTime.now());
		return new DefaultOidcUser(userRepository.save(user), oidcUser);
	}
}
