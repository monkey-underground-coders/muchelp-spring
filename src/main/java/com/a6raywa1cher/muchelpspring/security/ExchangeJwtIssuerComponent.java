package com.a6raywa1cher.muchelpspring.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class ExchangeJwtIssuerComponent {
	private final static String ISSUER_NAME = "muchelp-spring";
	private final static String VENDOR_ID_CLAIM = "vid";
	private final static String VENDOR_NAME_CLAIM = "vnm";
	private final static String TOKEN_TYPE = "ttp";
	private final static String TOKEN_TYPE_VALUE = "exchange";
	@Value("${jwt.secret}")
	private String secret;
	private Algorithm algorithm;
	private JWTVerifier jwtVerifier;
	@Value("${jwt.exchange-duration}")
	private long duration;

	@PostConstruct
	public void init() {
		algorithm = Algorithm.HMAC512(secret);
		jwtVerifier = JWT.require(algorithm)
				.withIssuer(ISSUER_NAME)
				.withClaim(TOKEN_TYPE, TOKEN_TYPE_VALUE)
				.build();
	}

	public String issue(OAuth2User oAuth2User) {
		if (!(oAuth2User instanceof OAuthUserInfo)) {
			throw new IllegalArgumentException("Not OAuthUserInfo");
		}
		OAuthUserInfo oAuthUserInfo = (OAuthUserInfo) oAuth2User;
		String vendorName = oAuthUserInfo.getVendor();
		String vendorId = oAuthUserInfo.getVendorId();
		Long userId = oAuthUserInfo.getId();
		return JWT.create()
				.withIssuer(ISSUER_NAME)
				.withSubject(Long.toString(userId))
				.withClaim(VENDOR_ID_CLAIM, vendorId)
				.withClaim(VENDOR_NAME_CLAIM, vendorName)
				.withClaim(TOKEN_TYPE, TOKEN_TYPE_VALUE)
				.withExpiresAt(Date.from(ZonedDateTime.now().plus(duration, ChronoUnit.SECONDS).toInstant()))
				.sign(algorithm);
	}

	public Optional<ExchangeJwtBody> decode(String token) {
		try {
			DecodedJWT decodedJWT = jwtVerifier.verify(token);
			ExchangeJwtBody exchangeJwtBody = new ExchangeJwtBody();
			exchangeJwtBody.setUid(Long.parseLong(decodedJWT.getSubject()));
			exchangeJwtBody.setOAuthVendor(decodedJWT.getClaim(VENDOR_NAME_CLAIM).asString());
			exchangeJwtBody.setVendorId(decodedJWT.getClaim(VENDOR_ID_CLAIM).asString());
			return Optional.of(exchangeJwtBody);
		} catch (JWTVerificationException e) {
			return Optional.empty();
		}
	}
}
