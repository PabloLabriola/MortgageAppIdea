package com.cognizant.feesmodule.services;

import com.cognizant.feesmodule.exceptions.FeeRepositoryException;
import com.cognizant.feesmodule.exceptions.NotAValidMortgageException;
import com.cognizant.feesmodule.model.dtos.FeeEventDto;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;

public interface FeesService {

    FeeEventDto processFees (MortgageEventDto mortgageDto) throws NotAValidMortgageException, FeeRepositoryException;

}
