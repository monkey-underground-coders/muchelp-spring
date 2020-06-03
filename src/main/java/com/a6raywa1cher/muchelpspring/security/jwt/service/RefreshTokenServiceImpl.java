package com.a6raywa1cher.muchelpspring.security.jwt.service;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;
import com.a6raywa1cher.muchelpspring.security.model.repo.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final RefreshTokenRepository repository;

	public RefreshTokenServiceImpl(RefreshTokenRepository repository) {
		this.repository = repository;
	}

	@Override
	public RefreshToken issue(User user) {
		List<RefreshToken> tokenList = repository.findAllByUser(user);
		if (tokenList.size() > 5) {
			repository.deleteAll(tokenList.stream()
					.sorted(Comparator.comparing(RefreshToken::getExpiringAt))
					.limit(tokenList.size() - 5)
					.collect(Collectors.toUnmodifiableList()));
		}
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setExpiringAt(LocalDateTime.now().plus(3, ChronoUnit.MONTHS));
		refreshToken.setUser(user);
		return repository.save(refreshToken);
	}

	@Override
	public Optional<RefreshToken> getByToken(String token) {
		return repository.findById(token);
	}

	@Override
	public void invalidate(RefreshToken refreshToken) {
		repository.delete(refreshToken);
	}

	@Override
	public void invalidateAll(User user) {
		repository.deleteAll(repository.findAllByUser(user));
	}
}
