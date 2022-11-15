package com.cognizant.companymanagement.security.model;

import lombok.Data;

@Data
public class TokenRefreshResponse {
	private String accessToken;
	private String refreshToken;
	private final String BEARER_PREFIX = "Bearer ";

	public TokenRefreshResponse(String accessToken, String refreshToken) {
		this.accessToken = BEARER_PREFIX + accessToken;
		this.refreshToken = BEARER_PREFIX + refreshToken;
	}
}
