package com.cognizant.mortgagefeasibilityvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Mortgage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false)
    private Request request;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private InterestType interestType;
    private BigDecimal interestRate;
    private BigDecimal monthlyPayment;
    @NotNull
    @Enumerated(value = EnumType.STRING)
    private MortgageStatus state;
    private String country;
    private String city;
    private BigDecimal homePrice;
    private BigDecimal downPayment;
    private int loanLength;
    private BigDecimal monthlyIncomes;

}
