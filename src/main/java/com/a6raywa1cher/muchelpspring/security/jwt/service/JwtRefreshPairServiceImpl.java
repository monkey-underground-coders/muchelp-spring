package com.a6raywa1cher.muchelpspring.security.jwt.service;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtRefreshPair;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JwtRefreshPairServiceImpl implements JwtRefreshPairService {
	private final RefreshTokenService refreshTokenService;

	private final JwtTokenService jwtTokenService;

	public JwtRefreshPairServiceImpl(RefreshTokenService refreshTokenService, JwtTokenService jwtTokenService) {
		this.refreshTokenService = refreshTokenService;
		this.jwtTokenService = jwtTokenService;
	}

	@Override
	public JwtRefreshPair issue(User user) {
		JwtToken accessToken = jwtTokenService.issue(user.getId());
		RefreshToken refreshToken = refreshTokenService.issue(user);
		return new JwtRefreshPair(
				refreshToken.getToken(),
				OffsetDateTime.of(refreshToken.getExpiringAt(), OffsetDateTime.now().getOffset()),
				accessToken.getToken(),
				OffsetDateTime.of(accessToken.getExpiringAt(), OffsetDateTime.now().getOffset())
		);
	}

	@Override
	public JwtRefreshPair issue(User user, VendorId vendorId, String sub) {
		JwtToken accessToken = jwtTokenService.issue(vendorId, sub, user.getId());
		RefreshToken refreshToken = refreshTokenService.issue(user);
		return new JwtRefreshPair(
				refreshToken.getToken(),
				OffsetDateTime.of(refreshToken.getExpiringAt(), OffsetDateTime.now().getOffset()),
				accessToken.getToken(),
				OffsetDateTime.of(accessToken.getExpiringAt(), OffsetDateTime.now().getOffset())
		);
	}
}
