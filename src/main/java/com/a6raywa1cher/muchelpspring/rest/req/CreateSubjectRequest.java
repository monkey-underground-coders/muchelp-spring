package com.a6raywa1cher.muchelpspring.rest.req;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CreateSubjectRequest {
	@Pattern(regexp = "^[0-9a-zA-Zа-яА-Я .]{3,64}$")
	private String name;
}
