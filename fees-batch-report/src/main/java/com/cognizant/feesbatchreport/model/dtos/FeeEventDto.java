package com.cognizant.feesbatchreport.model.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FeeEventDto {
    private String company;
    private UUID mortgageId;
    private BigDecimal feeCalculation;
    private BigDecimal  totalAmount;
}
