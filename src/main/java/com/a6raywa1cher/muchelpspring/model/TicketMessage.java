package com.a6raywa1cher.muchelpspring.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class TicketMessage {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User sender;

	@ManyToOne
	private Ticket ticket;

	@Column(length = 512)
	private String message;

	@ElementCollection
	private List<String> attachmentPaths;

	@Column
	private LocalDateTime sentAt;
}
