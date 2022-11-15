package com.cognizant.mortgagefeasibilityvalidator.mappers;

import com.cognizant.mortgagefeasibilityvalidator.model.Mortgage;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageDetailsRequestModel;
import org.mapstruct.Mapper;


@Mapper
public interface MortgageDetailsMapper {

    Mortgage mortgageRequestModelToMortgageEntity(MortgageDetailsRequestModel entity);
}
