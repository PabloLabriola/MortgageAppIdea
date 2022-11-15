package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.MortgageConfirmedEvent;

public interface ConfirmationSendQueueService {
    public void sendQueue(MortgageConfirmedEvent mortgageConfirmedEvent);
}
