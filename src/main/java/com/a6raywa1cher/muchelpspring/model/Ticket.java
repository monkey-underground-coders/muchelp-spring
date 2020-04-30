package com.a6raywa1cher.muchelpspring.model;

import com.a6raywa1cher.muchelpspring.utils.Views;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class Ticket {
	@Id
	@GeneratedValue
	@JsonView(Views.Public.class)
	private Long id;

	@OneToOne(mappedBy = "lastTicket")
	@JsonView(Views.Public.class)
	private User ticketIssuer;

	@ElementCollection
	@MapKeyClass(User.class)
	@Enumerated(value = EnumType.STRING)
	@JsonView(Views.Public.class)
	private Map<User, TicketStatus> statusMap;

	@Column(length = 1024)
	@JsonView(Views.Public.class)
	private String message;

	@ElementCollection
	@JsonView(Views.Public.class)
	private List<String> attachmentPaths;

	@ManyToOne
	@JsonView(Views.Public.class)
	private Subject subject;
}
