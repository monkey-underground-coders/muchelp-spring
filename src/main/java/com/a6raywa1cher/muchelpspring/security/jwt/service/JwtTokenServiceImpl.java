package com.a6raywa1cher.muchelpspring.security.jwt.service;

import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenServiceImpl implements JwtTokenService {
	private final static String ISSUER_NAME = "muchelp-spring";
	private final static String VENDOR_ID_CLAIM = "vid";
	private final static String VENDOR_SUB_CLAIM = "vsub";
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
				.build();
	}

	@Override
	public JwtToken issue(Long userId) {
		ZonedDateTime expiringAt = nowPlusDuration();
		String token = JWT.create()
				.withIssuer(ISSUER_NAME)
				.withSubject(Long.toString(userId))
				.withExpiresAt(Date.from(expiringAt.toInstant()))
				.sign(algorithm);
		return JwtToken.builder()
				.token(token)
				.uid(userId)
				.expiringAt(expiringAt.toLocalDateTime())
				.build();
	}

	private ZonedDateTime nowPlusDuration() {
		return ZonedDateTime.now().plus(duration, ChronoUnit.SECONDS);
	}

	@Override
	public JwtToken issue(VendorId vendorId, String vendorSub, Long userId) {
		ZonedDateTime zonedDateTime = nowPlusDuration();
		String token = JWT.create()
				.withIssuer(ISSUER_NAME)
				.withSubject(Long.toString(userId))
				.withClaim(VENDOR_SUB_CLAIM, vendorSub)
				.withClaim(VENDOR_ID_CLAIM, vendorId.toString())
				.withExpiresAt(Date.from(zonedDateTime.toInstant()))
				.sign(algorithm);
		return JwtToken.builder()
				.token(token)
				.uid(userId)
				.expiringAt(zonedDateTime.toLocalDateTime())
				.vendorId(vendorId)
				.vendorSub(vendorSub)
				.build();
	}

	@Override
	public Optional<JwtToken> decode(String token) {
		try {
			DecodedJWT decodedJWT = jwtVerifier.verify(token);
			JwtToken jwtToken = JwtToken.builder()
					.token(token)
					.uid(Long.parseLong(decodedJWT.getSubject()))
					.expiringAt(LocalDateTime.from(decodedJWT.getExpiresAt().toInstant()))
					.build();
			if (!decodedJWT.getClaim(VENDOR_SUB_CLAIM).isNull()) {
				jwtToken.setVendorId(VendorId.valueOf(decodedJWT.getClaim(VENDOR_ID_CLAIM).asString()));
				jwtToken.setVendorSub(decodedJWT.getClaim(VENDOR_SUB_CLAIM).asString());
			}
			return Optional.of(jwtToken);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}
