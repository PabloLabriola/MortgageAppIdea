package com.cognizant.companymanagement.security.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtError {
    private final Integer errorCode;
    private final String errorLabel;

    public JwtError(Integer errorCode, String errorLabel, String errorMessage, String value) {
        this.errorCode = errorCode;
        this.errorLabel = errorLabel;
        this.errorMessage = errorMessage;
        this.value = value;
    }

    private final String errorMessage;
    private final String value;
}
