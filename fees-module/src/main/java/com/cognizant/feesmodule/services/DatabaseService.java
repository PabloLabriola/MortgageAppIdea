package com.cognizant.feesmodule.services;

import com.cognizant.feesmodule.model.Fee;
import com.cognizant.feesmodule.model.dtos.FeeEventDto;

public interface DatabaseService {
     Fee saveFee(FeeEventDto feeDto);
}
