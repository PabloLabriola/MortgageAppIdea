package com.cognizant.feesmodule.services;

import com.cognizant.feesmodule.eventcontroller.FeesEventsProducer;
import com.cognizant.feesmodule.exceptions.FeeRepositoryException;
import com.cognizant.feesmodule.exceptions.NotAValidMortgageException;
import com.cognizant.feesmodule.model.Fee;
import com.cognizant.feesmodule.model.dtos.FeeEventDto;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import com.cognizant.feesmodule.repository.FeesRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Log4j2
@Service
public class FeesServiceImpl implements FeesService {
    private final ModelMapper modelMapper;
    private final FeesEventsProducer feesEventsProducer;
    private final BigDecimal approvedFeeMultiplier;
    private final BigDecimal rejectedOnHoldMultiplier;
    private final DatabaseService databaseService;

    public FeesServiceImpl(FeesEventsProducer feesEventsProducer,
                           @Value("${approved.fee.multiplier}") BigDecimal approvedFeeMultiplier,
                           @Value("${rejectedonhold.fee.multiplier}") BigDecimal rejectedOnHoldMultiplier,
                           DatabaseService databaseService, ModelMapper modelMapper) {
        this.feesEventsProducer = feesEventsProducer;
        this.approvedFeeMultiplier = approvedFeeMultiplier;
        this.rejectedOnHoldMultiplier = rejectedOnHoldMultiplier;
        this.databaseService =  databaseService;
        this.modelMapper = modelMapper;
    }

    @Override
    public FeeEventDto processFees (MortgageEventDto mortgageDto) throws NotAValidMortgageException, FeeRepositoryException {
        FeeEventDto feeDto = constructFeeEvent(mortgageDto);
        Fee savedFee = databaseService.saveFee(feeDto);
        FeeEventDto savedFeeDto = toFeeEventDto(savedFee);
        if(savedFee.getId()!=null) {
            sendFeeEvent(savedFeeDto);
        } else {
            throw new FeeRepositoryException();
        }
        return savedFeeDto;
    }

    private void sendFeeEvent(FeeEventDto feeDto) {
        feesEventsProducer.sendFeeEvent(feeDto);
    }

    private BigDecimal calculateFee(MortgageEventDto mortgageDto) throws NotAValidMortgageException {

        log.debug("Calculating fee price " + mortgageDto.toString());
        BigDecimal calculatedFee;
        switch(mortgageDto.getMortgageStatus())
        {
            case APPROVED:
                calculatedFee = mortgageDto.getHomePrice().multiply(approvedFeeMultiplier);
                break;

            case REJECTED:
                calculatedFee = mortgageDto.getHomePrice().multiply(rejectedOnHoldMultiplier);
                break;

            case HOLD:
                calculatedFee = mortgageDto.getHomePrice().multiply(rejectedOnHoldMultiplier);
                break;

            // Not an accepted fee status
            default:
                throw new NotAValidMortgageException(mortgageDto);
        }
        log.debug("Calculated fee price " + calculatedFee);
        return calculatedFee;
    }

    private BigDecimal calculateTotalAmount(MortgageEventDto mortgageDto, BigDecimal feeValue){
        return mortgageDto.getHomePrice().add(feeValue);
    }


    private FeeEventDto constructFeeEvent(MortgageEventDto mortgageDto) throws NotAValidMortgageException {
        BigDecimal feeValue = calculateFee(mortgageDto);
        BigDecimal totalAmount = calculateTotalAmount(mortgageDto,feeValue);
        return FeeEventDto
                .builder()
                .mortgageId(mortgageDto.getMortgageId())
                .company(mortgageDto.getCompany())
                .feeCalculation(feeValue)
                .totalAmount(totalAmount)
                .build();
    }

    private FeeEventDto toFeeEventDto(Fee savedFee) {
        return  modelMapper.map(savedFee, FeeEventDto.class);
    }

}
