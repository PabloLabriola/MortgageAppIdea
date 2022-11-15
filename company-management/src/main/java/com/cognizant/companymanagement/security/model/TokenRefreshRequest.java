package com.cognizant.companymanagement.security.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenRefreshRequest {
	  @NotBlank
	  private String refreshToken;
	  
	  public TokenRefreshRequest refreshToken(String refreshToken) {
		  this.refreshToken = refreshToken;
		  return this;
	  }
}
