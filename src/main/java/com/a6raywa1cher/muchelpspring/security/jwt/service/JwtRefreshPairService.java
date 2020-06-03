package com.a6raywa1cher.muchelpspring.security.jwt.service;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.security.jwt.JwtRefreshPair;

public interface JwtRefreshPairService {
	JwtRefreshPair issue(User user);

	JwtRefreshPair issue(User user, VendorId vendorId, String sub);
}
