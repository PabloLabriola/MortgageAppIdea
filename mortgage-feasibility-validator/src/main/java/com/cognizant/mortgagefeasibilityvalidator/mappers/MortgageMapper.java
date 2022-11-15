package com.cognizant.mortgagefeasibilityvalidator.mappers;

import com.cognizant.mortgagefeasibilityvalidator.model.Mortgage;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageConfirmedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface MortgageMapper {
    @Mapping(target="mortgageId", source="id")
    MortgageConfirmedEvent mortgageToMortgageConfirmedEvent(Mortgage entity);

}
