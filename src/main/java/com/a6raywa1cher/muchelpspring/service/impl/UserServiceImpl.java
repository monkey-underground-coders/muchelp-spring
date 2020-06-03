package com.a6raywa1cher.muchelpspring.service.impl;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.VendorId;
import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import com.a6raywa1cher.muchelpspring.security.jwt.service.RefreshTokenService;
import com.a6raywa1cher.muchelpspring.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {
	private final UserRepository repository;
	private final RefreshTokenService refreshTokenService;

	public UserServiceImpl(UserRepository repository, RefreshTokenService refreshTokenService) {
		this.repository = repository;
		this.refreshTokenService = refreshTokenService;
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
	public User setVendorSub(User user, VendorId vendorId, String id) {
		if (this.getByVendorIdOrEmail(vendorId, id, "--not email!--").isPresent()) {
			throw new IllegalArgumentException();
		}
		switch (vendorId) {
			case VK:
				user.setVkId(id);
				break;
			case GOOGLE:
				user.setGoogleId(id);
				break;
			default:
				throw new RuntimeException();
		}
		return repository.save(user);
	}

	@Override
	@Transactional
	public User setLastVisit(User user, ZonedDateTime now) {
		user.setLastVisit(now);
		return repository.save(user);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void delete(User user) {
		refreshTokenService.invalidateAll(user);
		repository.delete(user);
	}
}
