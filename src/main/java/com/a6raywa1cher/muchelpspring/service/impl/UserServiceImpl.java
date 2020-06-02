package com.a6raywa1cher.muchelpspring.service.impl;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	public UserServiceImpl(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<User> getById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Optional<User> getByVendorIdOrEmail(VendorId vendorId, String id, String email) {
		switch (vendorId) {
			case VK:
				return repository.findByVkIdOrEmail(id, email);
			case GOOGLE:
				return repository.findByGoogleIdOrEmail(id, email);
			default:
				throw new RuntimeException();
		}
	}

	@Override
	@Transactional
	public User setLastVisit(User user, ZonedDateTime now) {
		user.setLastVisit(now);
		return repository.save(user);
	}
}
