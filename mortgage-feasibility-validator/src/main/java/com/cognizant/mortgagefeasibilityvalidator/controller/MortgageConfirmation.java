package com.cognizant.mortgagefeasibilityvalidator.controller;

import com.cognizant.mortgagefeasibilityvalidator.services.ConfirmationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MortgageFeasibilityValidator microservice rest controller
 */

@RestController
@RequestMapping("/confirm")
@Log4j2
public class MortgageConfirmation {

    /**
     * Business service
     */
    private final ConfirmationService confirmationService;

    public MortgageConfirmation(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    /**
     * Given a mortgage id, confirm a mortgage and respond
     *
     * @param mortgageId of the mortgage to confirm
     * @return a ResponseEntity<Response> with a Response code and description
     */
    @PostMapping
    public ResponseEntity<?> confirmMortgage(@RequestParam("mortgageId") String mortgageId) {
        log.info("Received request to confirm mortgage: " + mortgageId);
        return confirmationService.generateConfirmationResponse(mortgageId);
    }

}
