package com.cognizant.feesmodule.model.dtos;

import com.cognizant.feesmodule.model.MortgageStatus;
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
public class MortgageEventDto {
    private String company;
    private UUID mortgageId;
    private BigDecimal homePrice;
    private MortgageStatus mortgageStatus;
}
