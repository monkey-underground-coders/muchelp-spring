package com.a6raywa1cher.muchelpspring.service;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UserService {
	Optional<User> getById(Long id);

	Optional<User> getByVendorIdOrEmail(VendorId vendorId, String id, String email);

	User setLastVisit(User user, ZonedDateTime now);
}
