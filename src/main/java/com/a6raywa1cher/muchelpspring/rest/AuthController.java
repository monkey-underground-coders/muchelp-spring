package com.a6raywa1cher.muchelpspring.rest;

import com.a6raywa1cher.muchelpspring.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@GetMapping("/user")
	public Map<String, Object> user(@AuthenticationPrincipal User principal) {
		return Collections.singletonMap("name", principal.getName());
	}
}
