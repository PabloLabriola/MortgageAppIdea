package com.cognizant.legalsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

// Aggregate

@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
public class Mortgage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
