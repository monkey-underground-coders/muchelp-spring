package com.a6raywa1cher.muchelpspring.service;

import com.a6raywa1cher.muchelpspring.model.User;

import java.util.Optional;

public interface UserService {
	Optional<User> getById(Long id);
}
