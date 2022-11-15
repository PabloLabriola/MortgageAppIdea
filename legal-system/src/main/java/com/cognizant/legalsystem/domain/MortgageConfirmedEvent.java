package com.cognizant.legalsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class MortgageConfirmedEvent {
    private String company;
    private String country;
    private String city;
    private BigDecimal homePrice;
    private BigDecimal downPayment;
    private BigDecimal loanLength;
    private BigDecimal monthlyIncomes;
    private BigDecimal interestRate;
    private BigDecimal monthlyPayment;
    private UUID mortgageId;
}
