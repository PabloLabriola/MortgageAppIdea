package com.cognizant.feesmodule.services;

import com.cognizant.feesmodule.eventcontroller.FeesEventsProducer;
import com.cognizant.feesmodule.exceptions.FeeRepositoryException;
import com.cognizant.feesmodule.exceptions.NotAValidMortgageException;
import com.cognizant.feesmodule.model.Fee;
import com.cognizant.feesmodule.model.dtos.FeeEventDto;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import com.cognizant.feesmodule.model.MortgageStatus;
import com.cognizant.feesmodule.repository.FeesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FeesServiceUnitTest {

    @InjectMocks
    private FeesServiceImpl feesServiceImpl;

    @Mock
    private FeesRepository feesRepository;

    @Mock
    private FeesEventsProducer feesEventsProducer;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private final BigDecimal APPROVED_FEE_MULTIPLIER = BigDecimal.valueOf(0.01);

    @Mock
    private final BigDecimal REJECTED_ON_HOLD_MULTIPLIER = BigDecimal.valueOf(0.001);

    private MortgageEventDto mortgageEventDto;
    private final BigDecimal homePrice = BigDecimal.valueOf(100000);
    private BigDecimal rejectedOrOnHoldFeeCalculation;
    private BigDecimal approvedFeeCalculation;
    private BigDecimal totalApprovedAmount;
    private BigDecimal totalRejectedOnHoldAmount;
    private Fee mapperFee;
    private FeeEventDto expectedFeeDto;
    private FeeEventDto mapperFeeEventDto;

    private UUID feeId = new UUID(0,2);

    @BeforeEach
    public void init() {
        UUID mortgageId = new UUID(0,1);
        mortgageEventDto = MortgageEventDto
                .builder()
                .company("CHOAM")
                .mortgageId(mortgageId)
                .homePrice(homePrice)
                .mortgageStatus(MortgageStatus.APPROVED)
                .build();

        approvedFeeCalculation = mortgageEventDto.getHomePrice().multiply(APPROVED_FEE_MULTIPLIER);
        rejectedOrOnHoldFeeCalculation = mortgageEventDto.getHomePrice().multiply(REJECTED_ON_HOLD_MULTIPLIER);
        totalApprovedAmount = mortgageEventDto.getHomePrice().add(approvedFeeCalculation);
        totalRejectedOnHoldAmount = mortgageEventDto.getHomePrice().add(rejectedOrOnHoldFeeCalculation);

        expectedFeeDto = FeeEventDto
                .builder()
                .mortgageId(mortgageEventDto.getMortgageId())
                .company(mortgageEventDto.getCompany())
                .build();

        mapperFeeEventDto = FeeEventDto
                .builder()
                .mortgageId(mortgageEventDto.getMortgageId())
                .company(mortgageEventDto.getCompany())
                .build();

        mapperFee = Fee
                .builder()
                .mortgageId(mortgageEventDto.getMortgageId())
                .build();
        doReturn(mapperFee).when(databaseService).saveFee(mapperFeeEventDto);
    }

    @Test
    void processesTheFeeCorrectlyWhenMortgageIsApprovedAndReturnsTheCreatedFeeEventDtoObject() throws FeeRepositoryException, NotAValidMortgageException {
        expectedFeeDto.setFeeCalculation(approvedFeeCalculation);
        expectedFeeDto.setTotalAmount(totalApprovedAmount);
        mapperFeeEventDto.setFeeCalculation(approvedFeeCalculation);
        mapperFeeEventDto.setTotalAmount(totalApprovedAmount);
        mapperFee.setId(feeId);
        when(modelMapper.map(any(Fee.class), any())).thenReturn(mapperFeeEventDto);
        assertEquals(feesServiceImpl.processFees(mortgageEventDto),expectedFeeDto);
    }


    @Test
    void processesTheFeeCorrectlyWhenMortgageIsRejectedAndReturnsTheCreatedFeeEventDtoObject() throws FeeRepositoryException, NotAValidMortgageException {
        expectedFeeDto.setFeeCalculation(rejectedOrOnHoldFeeCalculation);
        expectedFeeDto.setTotalAmount(totalRejectedOnHoldAmount);
        mapperFeeEventDto.setFeeCalculation(rejectedOrOnHoldFeeCalculation);
        mapperFeeEventDto.setTotalAmount(totalRejectedOnHoldAmount);
        mapperFee.setId(feeId);
        when(modelMapper.map(any(Fee.class), any())).thenReturn(mapperFeeEventDto);
        assertEquals(feesServiceImpl.processFees(mortgageEventDto),expectedFeeDto);
    }


    @Test
    void throwAnErrorWhenMortgageIsRejectedAndCantSaveFeeObject() throws FeeRepositoryException, NotAValidMortgageException {
        expectedFeeDto.setFeeCalculation(rejectedOrOnHoldFeeCalculation);
        expectedFeeDto.setTotalAmount(totalRejectedOnHoldAmount);
        mapperFeeEventDto.setFeeCalculation(rejectedOrOnHoldFeeCalculation);
        mapperFeeEventDto.setTotalAmount(totalRejectedOnHoldAmount);
        // We simulate not saving the object by not giving it an ID (mapperFee.setId(feeId));
        when(modelMapper.map(any(Fee.class), any())).thenReturn(mapperFeeEventDto);
        Assertions.assertThrows(FeeRepositoryException.class, () -> {
            feesServiceImpl.processFees(mortgageEventDto);
        });
    }

}