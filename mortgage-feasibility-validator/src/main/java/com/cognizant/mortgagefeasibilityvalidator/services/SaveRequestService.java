package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.MortgageDetailsRequestModel;
import com.cognizant.mortgagefeasibilityvalidator.model.Request;

public interface SaveRequestService {
    Request saveRequest(Request request);
}
