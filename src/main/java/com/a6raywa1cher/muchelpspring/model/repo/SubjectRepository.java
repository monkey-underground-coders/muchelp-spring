package com.a6raywa1cher.muchelpspring.model.repo;

import com.a6raywa1cher.muchelpspring.model.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
	Stream<Subject> findAllBy();

	Optional<Subject> findByName(String name);
}
