package com.cognizant.companymanagement.security.model;

import lombok.Data;

@Data
public class AccessTokenRequest {
	String accessToken;
	
	public AccessTokenRequest accessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}
}
