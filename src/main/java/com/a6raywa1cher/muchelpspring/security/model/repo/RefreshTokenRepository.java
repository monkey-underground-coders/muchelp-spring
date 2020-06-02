package com.a6raywa1cher.muchelpspring.security.model.repo;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.security.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Stream<RefreshToken> findAllByExpiringAtBefore(LocalDateTime expiringAt);

	List<RefreshToken> findAllByUser(User user);
}
