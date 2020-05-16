package com.a6raywa1cher.muchelpspring.model.repo;

import com.a6raywa1cher.muchelpspring.model.Subject;
import com.a6raywa1cher.muchelpspring.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	Optional<User> findByGoogleIdOrEmail(String googleId, String email);

	Optional<User> findByVkIdOrEmail(String vkId, String email);

	Optional<User> findByEmail(String email);

	Page<User> findByMySubjectsContaining(Subject mySubjects, Pageable pageable);
}
