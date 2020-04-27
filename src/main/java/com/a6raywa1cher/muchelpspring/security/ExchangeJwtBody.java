package com.a6raywa1cher.muchelpspring.security;

import lombok.Data;

@Data
public class ExchangeJwtBody {
	private String oAuthVendor;

	private long uid;

	private String vendorId;
}
