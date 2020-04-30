package com.a6raywa1cher.muchelpspring.rest;

import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.utils.AuthenticationResolver;
import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthenticationResolver authenticationResolver;

	public AuthController(AuthenticationResolver authenticationResolver) {
		this.authenticationResolver = authenticationResolver;
	}

	@GetMapping("/user")
	@JsonView(Views.Internal.class)
	public ResponseEntity<User> getCurrentUser() {
		User user = authenticationResolver.getUser();
		if (user == null) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return ResponseEntity.ok(user);
	}
}
