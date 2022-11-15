package com.cognizant.mortgagefeasibilityvalidator.services;

import org.springframework.http.ResponseEntity;

public interface ConfirmationService {
    ResponseEntity<?> generateConfirmationResponse(String mortgageId);
}
