package com.a6raywa1cher.muchelpspring.model.repo;

import com.a6raywa1cher.muchelpspring.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByGoogleIdOrEmail(String googleId, String email);

	Optional<User> findByVkIdOrEmail(String vkId, String email);

	Optional<User> findByEmail(String email);
}
