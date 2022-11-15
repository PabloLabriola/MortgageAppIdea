package com.cognizant.legalsystem.application.savemortgage;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import com.cognizant.legalsystem.domain.MortgageRepository;
import com.cognizant.legalsystem.domain.mappers.MortgageConfirmedEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
public class SaveMortgageServiceImpl implements SaveMortgageService{

    @Autowired
    private MortgageRepository mortgageRepository;

    private final MortgageConfirmedEventMapper mortgageConfirmedEventMapper = Mappers.getMapper(MortgageConfirmedEventMapper.class);

    @Override
    public void saveMortgage(MortgageConfirmedEvent mortgageConfirmedEvent) {

        Mortgage mortgage = mortgageConfirmedEventMapper.mortgageConfirmedEventMapperToMortgage(mortgageConfirmedEvent);

        mortgageRepository.saveMortgage(mortgage);
    }

}