package com.a6raywa1cher.muchelpspring.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Ticket {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	private User ticketIssuer;

	@ElementCollection
	@MapKeyClass(User.class)
	@Enumerated(value = EnumType.STRING)
	private Map<User, TicketStatus> statusMap;

	@Column(length = 1024)
	private String message;

	@ElementCollection
	private List<String> attachmentPaths;

	@ManyToOne
	private Subject subject;
}
