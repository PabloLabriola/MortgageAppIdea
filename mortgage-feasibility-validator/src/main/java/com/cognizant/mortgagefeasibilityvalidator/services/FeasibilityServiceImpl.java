package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.exceptions.MortgageDetailsException;
import com.cognizant.mortgagefeasibilityvalidator.mappers.MortgageDetailsMapper;
import com.cognizant.mortgagefeasibilityvalidator.model.*;
import com.cognizant.mortgagefeasibilityvalidator.model.dtos.MortgageEventDto;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class FeasibilityServiceImpl implements FeasibilityService {

    @Value("${IncomePercentage}")
    private BigDecimal incomePercentage;

    @Value("${MonthlyIncome.Threshold}")
    private BigDecimal monthlyIncomeThreshold;

    @Value("${HomePrice.Threshold}")
    private BigDecimal homePriceThreshold;

    @Value("${InterestRate.Fixed}")
    private BigDecimal fixedRate;

    @Value("${InterestRate.Variable}")
    private BigDecimal variableRate;

    private final SaveRequestService saveRequestService;
    private final MortgageEventPublisherService mortgageEventPublisherService;
    private final MortgageDetailsMapper mortgageDetailsMapper = Mappers.getMapper(MortgageDetailsMapper.class);


    public FeasibilityServiceImpl(
                                  SaveRequestService saveRequestService,
                                  MortgageEventPublisherService mortgageEventPublisherService
    ) {
        this.saveRequestService = saveRequestService;
        this.mortgageEventPublisherService= mortgageEventPublisherService;
    }

    @Override
    @Transactional
    public FeasibilityResponse generateFeasibilityResponse(MortgageDetailsRequestModel mortgageDetailsRequestModel) {

        if(mortgageDetailsRequestModel == null){
            throw new MortgageDetailsException("MortgageDetailsRequestModel null");
        } else if (mortgageDetailsRequestModel.getHomePrice() == null) {
            throw new MortgageDetailsException("HomePrice is null");
        }

        MortgageStatus mortgageStatus= checkFeasibilityStatus(mortgageDetailsRequestModel);
        Request request = saveRequest(createRequest(mortgageDetailsRequestModel, mortgageStatus));

        publishMortgageValidatedEvents(request, mortgageDetailsRequestModel, mortgageStatus);

        return new FeasibilityResponse(mortgageStatus, request.getMortgages());
    }

    private Request saveRequest(Request request){
        return saveRequestService.saveRequest(request);
    }

    private void publishMortgageValidatedEvents(Request request, MortgageDetailsRequestModel requestMortgageDetails, MortgageStatus status) {
        for(Mortgage mortgage : request.getMortgages()) {
            mortgageEventPublisherService.handleMortgageValidatedEvent(
                    new MortgageEventDto(requestMortgageDetails.getCompany(), mortgage.getId(), requestMortgageDetails.getHomePrice(), status)
            );
        }
    }


    public MortgageStatus checkFeasibilityStatus(MortgageDetailsRequestModel requestMortgageDetails) {

        if (requestMortgageDetails == null || requestMortgageDetails.getMonthlyIncomes() == null || requestMortgageDetails.getHomePrice() == null) {
            return MortgageStatus.REJECTED;
        }
        int result = requestMortgageDetails.getMonthlyIncomes().compareTo(monthlyIncomeThreshold);

        switch (result) {
            case 1:
                return MortgageStatus.APPROVED;
            case 0:
                return MortgageStatus.HOLD;
            default:
                return MortgageStatus.REJECTED;
        }

    }

    public final Request createRequest(MortgageDetailsRequestModel requestMortgageDetails, MortgageStatus status) {

        if(requestMortgageDetails.getHomePrice() == null){
            throw new MortgageDetailsException("HomePrice is null");
        }

        List<Mortgage> mortgages = new ArrayList<>();

        Request request = Request.builder()
                .company(requestMortgageDetails.getCompany())
                .mortgages(mortgages)
                .build();


        Mortgage fixedMortgage = mortgageDetailsMapper.mortgageRequestModelToMortgageEntity(requestMortgageDetails);
        fixedMortgage.setInterestType(InterestType.FIXED);
        fixedMortgage.setInterestRate(fixedRate);
        fixedMortgage.setRequest(request);
        fixedMortgage.setState(status);
        fixedMortgage.setMonthlyPayment(calculateMonthlyPayment(requestMortgageDetails.getMonthlyIncomes(), incomePercentage));
        mortgages.add(fixedMortgage);

        if (requestMortgageDetails.getHomePrice().compareTo(homePriceThreshold) < 0) {
            Mortgage variableMortgage = mortgageDetailsMapper.mortgageRequestModelToMortgageEntity(requestMortgageDetails);
            variableMortgage.setInterestType(InterestType.VARIABLE);
            variableMortgage.setInterestRate(variableRate);
            variableMortgage.setRequest(request);
            variableMortgage.setState(status);
            variableMortgage.setMonthlyPayment(calculateMonthlyPayment(requestMortgageDetails.getMonthlyIncomes(), incomePercentage));
            mortgages.add(variableMortgage);
        }

        return request;

    }

    public BigDecimal calculateMonthlyPayment(BigDecimal monthlyIncome, BigDecimal percentage) {
        return monthlyIncome != null ? monthlyIncome.multiply(percentage).setScale(2, RoundingMode.HALF_EVEN) : null;
    }


}
