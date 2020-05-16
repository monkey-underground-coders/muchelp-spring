package com.a6raywa1cher.muchelpspring.service.impl;

import com.a6raywa1cher.muchelpspring.model.Subject;
import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.model.repo.SubjectRepository;
import com.a6raywa1cher.muchelpspring.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SubjectServiceImpl implements SubjectService {
	private final SubjectRepository repository;

	public SubjectServiceImpl(SubjectRepository repository) {
		this.repository = repository;
	}

	@Override
	public Subject create(String name, User user) {
		Subject subject = new Subject();
		subject.setName(name);
		subject.setUserList(Collections.singletonList(user));
		return repository.save(subject);
	}

	@Override
	public Stream<Subject> getAll() {
		return repository.findAllBy();
	}

	@Override
	public Optional<Subject> getById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Optional<Subject> getByName(String name) {
		return repository.findByName(name);
	}

	@Override
	public Subject append(Subject subject, User user) {
		subject.getUserList().add(user);
		return repository.save(subject);
	}

	@Override
	public void pop(Subject subject, User user) {
		subject.getUserList().remove(user);
		if (subject.getUserList().size() == 0) {
			repository.delete(subject);
		} else {
			repository.save(subject);
		}
	}
}
