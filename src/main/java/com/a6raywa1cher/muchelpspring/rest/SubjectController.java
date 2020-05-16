package com.a6raywa1cher.muchelpspring.rest;

import com.a6raywa1cher.muchelpspring.model.Subject;
import com.a6raywa1cher.muchelpspring.model.User;
import com.a6raywa1cher.muchelpspring.rest.req.CreateSubjectRequest;
import com.a6raywa1cher.muchelpspring.service.SubjectService;
import com.a6raywa1cher.muchelpspring.utils.AuthenticationResolver;
import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/subject")
public class SubjectController {
	private final SubjectService subjectService;
	private final AuthenticationResolver resolver;

	public SubjectController(SubjectService subjectService, AuthenticationResolver resolver) {
		this.subjectService = subjectService;
		this.resolver = resolver;
	}

	@GetMapping("/all")
	@JsonView(Views.Public.class)
	public ResponseEntity<List<Subject>> getAllSubjects() {
		return ResponseEntity.ok(subjectService.getAll().collect(Collectors.toList()));
	}

	@GetMapping("/{id}")
	@JsonView(Views.Detailed.class)
	public ResponseEntity<Optional<Subject>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(subjectService.getById(id));
	}

	@PostMapping("/create")
	public ResponseEntity<Subject> createSubject(@RequestBody @Valid CreateSubjectRequest request) {
		User user = resolver.getUser();
		return ResponseEntity.ok(subjectService.create(request.getName(), user));
	}
}
