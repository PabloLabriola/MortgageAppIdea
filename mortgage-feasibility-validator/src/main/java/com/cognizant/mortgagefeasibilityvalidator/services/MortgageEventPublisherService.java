package com.cognizant.mortgagefeasibilityvalidator.services;


import com.cognizant.mortgagefeasibilityvalidator.model.dtos.MortgageEventDto;

public interface MortgageEventPublisherService {
    void handleMortgageValidatedEvent(MortgageEventDto mortgageValidatedEvent);
}
