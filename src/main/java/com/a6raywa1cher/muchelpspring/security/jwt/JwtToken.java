package com.a6raywa1cher.muchelpspring.security.jwt;

import com.a6raywa1cher.muchelpspring.model.VendorId;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JwtToken {
	private String token;

	private LocalDateTime expiringAt;

	private VendorId vendorId;

	private long uid;

	private String vendorSub;
}
