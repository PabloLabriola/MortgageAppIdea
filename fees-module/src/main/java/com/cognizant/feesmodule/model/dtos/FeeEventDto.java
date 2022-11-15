package com.cognizant.feesmodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeeEventDto {
    private String company;
    private UUID mortgageId;
    private BigDecimal feeCalculation;
    private BigDecimal totalAmount;
}
