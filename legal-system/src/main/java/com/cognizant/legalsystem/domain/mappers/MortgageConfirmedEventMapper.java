package com.cognizant.legalsystem.domain.mappers;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import org.mapstruct.Mapper;

@Mapper
public interface MortgageConfirmedEventMapper {

    Mortgage mortgageConfirmedEventMapperToMortgage(MortgageConfirmedEvent mortgageConfirmedEvent);
}
