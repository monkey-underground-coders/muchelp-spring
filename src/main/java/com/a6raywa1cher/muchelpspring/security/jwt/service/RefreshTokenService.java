package com.a6raywa1cher.muchelpspring.security.jwt.service;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
	RefreshToken issue(User user);

	Optional<RefreshToken> getByToken(String token);

	void invalidate(RefreshToken refreshToken);

	void invalidateAll(User user);
}
