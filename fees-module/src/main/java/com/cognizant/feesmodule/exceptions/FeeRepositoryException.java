package com.cognizant.feesmodule.exceptions;

public class FeeRepositoryException extends Exception {
    public FeeRepositoryException() {
        super("There was a problem saving Fee object.");
    }
}
