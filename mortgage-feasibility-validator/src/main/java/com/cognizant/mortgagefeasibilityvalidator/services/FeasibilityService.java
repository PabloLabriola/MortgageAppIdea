package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.FeasibilityResponse;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageDetailsRequestModel;


public interface FeasibilityService {

    FeasibilityResponse generateFeasibilityResponse(MortgageDetailsRequestModel requestMortgageDetails);

}
