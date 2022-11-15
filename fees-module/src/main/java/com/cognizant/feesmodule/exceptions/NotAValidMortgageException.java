package com.cognizant.feesmodule.exceptions;

import com.cognizant.feesmodule.model.dtos.MortgageEventDto;

public class NotAValidMortgageException extends Exception {
    public NotAValidMortgageException(MortgageEventDto mortgage) {
        super("Not a valid mortgage: " + mortgage.toString());
    }
}
