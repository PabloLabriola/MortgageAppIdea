package com.cognizant.companymanagement.security.model;

import lombok.Data;

@Data
public class JwtResponse {
	private String accessToken;
	private String refreshToken;
	private final String BEARER_PREFIX = "Bearer ";
	private String companyName;
	
	public JwtResponse(String accessToken, String refreshToken, String companyName) {
		this.accessToken = BEARER_PREFIX + accessToken;
		this.refreshToken = BEARER_PREFIX + refreshToken;
		this.companyName = companyName;
	}
}
