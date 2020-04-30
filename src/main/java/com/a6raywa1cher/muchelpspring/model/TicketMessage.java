package com.a6raywa1cher.muchelpspring.model;

import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class TicketMessage {
	@Id
	@GeneratedValue
	@JsonView(Views.Public.class)
	private Long id;

	@ManyToOne
	@JsonView(Views.Public.class)
	private User sender;

	@ManyToOne
	@JsonView(Views.Public.class)
	private Ticket ticket;

	@Column(length = 512)
	@JsonView(Views.Public.class)
	private String message;

	@ElementCollection
	@JsonView(Views.Public.class)
	private List<String> attachmentPaths;

	@Column
	@JsonView(Views.Public.class)
	private LocalDateTime sentAt;
}
