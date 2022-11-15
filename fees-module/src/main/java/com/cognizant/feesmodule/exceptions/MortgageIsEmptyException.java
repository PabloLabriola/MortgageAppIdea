package com.cognizant.feesmodule.exceptions;

public class MortgageIsEmptyException extends Exception {
    public MortgageIsEmptyException() {
        super("Mortgage object is empty.");
    }
}
