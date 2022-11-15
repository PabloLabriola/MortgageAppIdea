package com.cognizant.feesmodule.services;

import com.cognizant.feesmodule.model.Fee;
import com.cognizant.feesmodule.model.dtos.FeeEventDto;
import com.cognizant.feesmodule.repository.FeesRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DatabaseServiceImpl implements DatabaseService {
    private final ModelMapper modelMapper;
    private final FeesRepository feesRepository;

    public DatabaseServiceImpl(ModelMapper modelMapper, FeesRepository feesRepository) {
        this.modelMapper = modelMapper;
        this.feesRepository = feesRepository;
    }

    @Override
    public Fee saveFee(FeeEventDto feeDto) {
        Fee fee = modelMapper.map(feeDto, Fee.class);
        log.debug("Saving fee entity: " + fee.toString());
        Fee savedFee = feesRepository.save(fee);
        log.debug("Saved fee: " + savedFee);
        return savedFee;
    }
}
