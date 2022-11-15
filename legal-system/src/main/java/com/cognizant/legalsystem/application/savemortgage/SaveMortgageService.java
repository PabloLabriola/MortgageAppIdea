package com.cognizant.legalsystem.application.savemortgage;

import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;

public interface SaveMortgageService {
    void saveMortgage(MortgageConfirmedEvent mortgageConfirmedEvent);
}
