package com.a6raywa1cher.muchelpspring.service;

import com.a6raywa1cher.muchelpspring.model.Subject;
import com.a6raywa1cher.muchelpspring.model.User;

import java.util.Optional;
import java.util.stream.Stream;

public interface SubjectService {
	Subject create(String name, User user);

	Stream<Subject> getAll();

	Optional<Subject> getById(Long id);

	Optional<Subject> getByName(String name);

	Subject append(Subject subject, User user);

	void pop(Subject subject, User user);
}
