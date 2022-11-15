package com.cognizant.mortgagefeasibilityvalidator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MortgageDetailsRequestModel {

    private String company;
    private String country;
    private String city;
    private BigDecimal homePrice;
    private BigDecimal downPayment;
    private int loanLength;
    private BigDecimal monthlyIncomes;

}
