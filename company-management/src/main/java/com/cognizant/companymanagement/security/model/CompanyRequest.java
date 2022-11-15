package com.cognizant.companymanagement.security.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CompanyRequest {
	@NotBlank
	private String companyName;
	
	public CompanyRequest companyName(String companyName) {
		this.companyName = companyName;
		return this;
	}
}
