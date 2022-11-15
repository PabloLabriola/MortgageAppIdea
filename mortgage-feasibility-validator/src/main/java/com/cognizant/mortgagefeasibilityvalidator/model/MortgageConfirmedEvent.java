package com.cognizant.mortgagefeasibilityvalidator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MortgageConfirmedEvent implements Serializable {
    private String company;
    private String country;
    private String city;
    private BigDecimal homePrice;
    private BigDecimal downPayment;
    private int loanLength;
    private BigDecimal monthlyIncomes;
    private BigDecimal interestRate;
    private BigDecimal monthlyPayment;
    private UUID mortgageId;
}
