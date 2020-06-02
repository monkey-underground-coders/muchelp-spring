package com.a6raywa1cher.muchelpspring.security.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component("oauth2-user-service")
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final RestTemplate restTemplate = new RestTemplate();

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
		if (!StringUtils.isEmpty(additionalParameters.getOrDefault("deactivated", ""))) {
			throw new OAuth2AuthenticationException(new OAuth2Error("Unverified email"));
		}
		String vkId = (String) additionalParameters.get("user_id");
		String email = (String) additionalParameters.get("email");
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("sub", vkId);
		attributes.put("email", email);
		ResponseEntity<ObjectNode> responseEntity = restTemplate.getForEntity(
				"https://api.vk.com/method/users.get?access_token={access_token}&v=5.103&user_ids={user_ids}&fields=photo_200",
				ObjectNode.class,
				Map.of("access_token", userRequest.getAccessToken().getTokenValue(),
						"user_ids", vkId
				)
		);
		if (responseEntity.getBody() == null || !responseEntity.getBody().has("response")) {
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_SCOPE),
					String.format("Empty body or error on getting UserInfo from %s",
							userRequest.getClientRegistration().getRegistrationId()));
		}
		JsonNode additionalInfo = responseEntity.getBody().get("response").get(0);
		attributes.put("name", String.join(" ", additionalInfo.get("first_name").asText(), additionalInfo.get("last_name").asText()));
		attributes.put("picture", additionalInfo.get("photo_200").asText());
		return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
				attributes, "sub");
	}
}
