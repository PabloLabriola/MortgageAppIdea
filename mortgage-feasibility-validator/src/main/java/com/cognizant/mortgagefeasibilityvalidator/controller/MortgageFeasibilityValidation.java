package com.cognizant.mortgagefeasibilityvalidator.controller;

import com.cognizant.mortgagefeasibilityvalidator.model.FeasibilityResponse;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageDetailsRequestModel;
import com.cognizant.mortgagefeasibilityvalidator.services.FeasibilityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MortgageFeasibilityValidator microservice rest controller
 */
@Log4j2
@RestController
@RequestMapping("/validate")
public class MortgageFeasibilityValidation {

    /**
     * Business service
     */
    private final FeasibilityService feasibilityService;

    public MortgageFeasibilityValidation(FeasibilityService feasibilityService) {
        this.feasibilityService = feasibilityService;
    }

    /**
     * Given a mortgage details, return feasibility study response
     *
     * @param requestMortgageDetails JSON with mortgage details
     * @return a ResponseEntity<Response> with a Response code and description
     */
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public FeasibilityResponse validateMortgage(@RequestBody MortgageDetailsRequestModel requestMortgageDetails) {
        log.info("Received request to validate mortgage request from: " + requestMortgageDetails.getCompany());
        return feasibilityService.generateFeasibilityResponse(requestMortgageDetails);
    }

}
