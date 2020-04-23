package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

@Component("oauth2-user-service")
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserRepository userRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	public OAuth2UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
		String vkId = (String) additionalParameters.get("user_id");
		String email = (String) additionalParameters.get("email");
		User user = userRepository.findByVkIdOrEmail(vkId, email).orElseGet(() -> {
			if (!StringUtils.isEmpty(additionalParameters.getOrDefault("deactivated", ""))) {
				throw new OAuth2AuthenticationException(new OAuth2Error("Unverified email"));
			}
			ResponseEntity<ObjectNode> responseEntity = restTemplate.getForEntity(
					"https://api.vk.com/method/users.get?access_token={access_token}&v=5.103&user_ids={user_ids}&fields=photo_200",
					ObjectNode.class,
					Map.of("access_token", userRequest.getAccessToken().getTokenValue(),
							"user_ids", vkId
					)
			);
			JsonNode additionalInfo = responseEntity.getBody().get("response").get(0);
			User user1 = new User();
			user1.setVkId(vkId);
			user1.setCreatedAt(LocalDateTime.now());
			user1.setEmail((String) additionalParameters.get("email"));
			user1.setLastTicket(null);
			user1.setMySubjects(new ArrayList<>());
			user1.setName(String.join(" ", additionalInfo.get("first_name").asText(), additionalInfo.get("last_name").asText()));
			user1.setPicture(additionalInfo.get("photo_200").asText());
			return user1;
		});
		user.setVkId(vkId);
		user.setLastVisit(LocalDateTime.now());
		return new DefaultOAuth2User(userRepository.save(user), additionalParameters,
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}
}
