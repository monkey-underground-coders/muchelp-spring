package com.a6raywa1cher.muchelpspring.security.jwt.service;

import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtToken;

import java.util.Optional;

public interface JwtTokenService {
	JwtToken issue(Long userId);

	JwtToken issue(VendorId vendorId, String vendorSub, Long userId);

	Optional<JwtToken> decode(String token);
}
