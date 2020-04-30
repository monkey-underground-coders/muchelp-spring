package com.a6raywa1cher.muchelpspring.model;

import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Subject {
	@Id
	@GeneratedValue
	@JsonView(Views.Public.class)
	private Long id;

	@Column(unique = true)
	@JsonView(Views.Public.class)
	private String name;

	@ManyToMany
	@JsonView(Views.Public.class)
	private List<User> userList;

	@OneToMany(mappedBy = "subject")
	@JsonView(Views.Public.class)
	private List<Ticket> tickets;
}
