package com.a6raywa1cher.muchelpspring.security.model;

import com.a6raywa1cher.muchelpspring.model.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Data
public class RefreshToken {
	@Id
	private String token;

	@Column
	private LocalDateTime expiringAt;

	@ManyToOne
	private User user;
}
