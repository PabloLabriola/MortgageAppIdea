package com.cognizant.mortgagefeasibilityvalidator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeasibilityResponse {
    @NonNull
    private MortgageStatus state;
    private List<Mortgage> mortgages;

    public FeasibilityResponse(MortgageStatus state) {
        this.state = state;
    }

}
