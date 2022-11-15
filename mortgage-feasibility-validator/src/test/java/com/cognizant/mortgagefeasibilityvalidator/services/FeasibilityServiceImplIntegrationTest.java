package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.*;
import com.cognizant.mortgagefeasibilityvalidator.model.dtos.MortgageEventDto;
import com.cognizant.mortgagefeasibilityvalidator.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles({"integration","persistence"})
class FeasibilityServiceImplIntegrationTest {

    @Autowired
    private FeasibilityService feasibilityService;

    @Value("${MonthlyIncome.Threshold}")
    private BigDecimal monthlyIncomeThreshold;

    @Value("${HomePrice.Threshold}")
    private BigDecimal homePriceThreshold;

    @Value("${IncomePercentage}")
    private BigDecimal incomePercentage;

    @Value("${InterestRate.Fixed}")
    private BigDecimal fixedRate;

    @Value("${InterestRate.Variable}")
    private BigDecimal variableRate;

    private MortgageDetailsRequestModel mortgageDetailsRequestModel;

    @MockBean
    private MortgageEventPublisherService mortgageEventPublisherService;

    @MockBean
    private ConfirmationSendQueueService confirmationSendQueueService;

    @Autowired
    private RequestRepository requestRepository;


    @Nested
    class CheckFeasibility {

        Mortgage expectedFixedMortgage;
        Mortgage expectedVariableMortgage;

        @BeforeEach
        void testInitialization() {
            mortgageDetailsRequestModel = MortgageDetailsRequestModel
                    .builder()
                    .company("subscribed")
                    .country("Spain")
                    .city("Madrid")
                    .homePrice(BigDecimal.valueOf(100000))
                    .downPayment(BigDecimal.valueOf(1000))
                    .loanLength(25)
                    .monthlyIncomes(monthlyIncomeThreshold).build();

            expectedFixedMortgage = Mortgage
                    .builder()
                    .interestType(InterestType.FIXED)
                    .interestRate(fixedRate.setScale(2, RoundingMode.CEILING))
                    .state(MortgageStatus.APPROVED)
                    .monthlyPayment(new BigDecimal("30.00"))
                    .city("Madrid")
                    .country("Spain")
                    .downPayment(new BigDecimal("1000.00"))
                    .homePrice(new BigDecimal("100000.00"))
                    .loanLength(25)
                    .monthlyIncomes(monthlyIncomeThreshold.setScale(2, RoundingMode.CEILING))
                    .build();

            expectedVariableMortgage = Mortgage
                    .builder()
                    .interestType(InterestType.VARIABLE)
                    .interestRate(variableRate.setScale(2, RoundingMode.CEILING))
                    .state(MortgageStatus.APPROVED)
                    .monthlyPayment(new BigDecimal("30.00"))
                    .city("Madrid")
                    .country("Spain")
                    .downPayment(new BigDecimal("1000.00"))
                    .homePrice(new BigDecimal("100000.00"))
                    .loanLength(25)
                    .monthlyIncomes(monthlyIncomeThreshold.setScale(2, RoundingMode.CEILING))
                    .build();

            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));
        }

        @Test
        void shouldSaveARequestWithFixedTypeMortgage_WhenMonthlyIncomeHigherThanThresholdAndHomePriceHigherThanThreshold() throws Exception {
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(0.00000000000000001)));
            FeasibilityResponse actual = feasibilityService.generateFeasibilityResponse(mortgageDetailsRequestModel);
            UUID requestId = actual.getMortgages().get(0).getRequest().getId();

            Request request = requestRepository.findById(requestId).orElseThrow(()-> new Exception("The request couldn't be found on the repository"));

            assertEquals(1, request.getMortgages().size());
            Mortgage actualMortgage = request.getMortgages().get(0);
            assertEquals(InterestType.FIXED, actualMortgage.getInterestType());

            //Check all database fields
            assertNotNull(actualMortgage.getId());//Generated by DB
            expectedFixedMortgage.setId(actualMortgage.getId());
            assertEquals(expectedFixedMortgage, request.getMortgages().get(0));
        }

        @Test
        void shouldSaveARequestWithFixedTypeMortgage_WhenMonthlyIncomeHigherThanThresholdAndHomePriceEqualThanThreshold() throws Exception {
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(0.00000000000000001)));
            FeasibilityResponse actual = feasibilityService.generateFeasibilityResponse(mortgageDetailsRequestModel);
            UUID requestId = actual.getMortgages().get(0).getRequest().getId();

            Request request = requestRepository.findById(requestId).orElseThrow(()-> new Exception("The request couldn't be found on the repository"));

            assertEquals(1, request.getMortgages().size());
            Mortgage actualMortgage = request.getMortgages().get(0);
            assertEquals(InterestType.FIXED, actualMortgage.getInterestType());

            //Check all database fields
            assertNotNull(actualMortgage.getId());//Generated by DB
            expectedFixedMortgage.setId(actualMortgage.getId());
            assertEquals(expectedFixedMortgage, request.getMortgages().get(0));
        }

        @Test
        void shouldSaveTwoRequestsWithFixedAndFixedTypeMortgage_WhenMonthlyIncomeHigherThanThresholdAndHomePriceEqualThanThreshold() throws Exception {
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().subtract(BigDecimal.valueOf(0.00000000000000001)));
            FeasibilityResponse actual = feasibilityService.generateFeasibilityResponse(mortgageDetailsRequestModel);
            UUID requestId = actual.getMortgages().get(0).getRequest().getId();

            Request request = requestRepository.findById(requestId).orElseThrow(()-> new Exception("The request couldn't be found on the repository"));

            assertEquals(2, request.getMortgages().size());
            Optional<Mortgage> actualFixedMortgage = request.getMortgages().stream().filter(m -> m.getInterestType().equals(InterestType.FIXED)).findAny();
            assertTrue(actualFixedMortgage.isPresent());
            Optional<Mortgage> actualVariableMortgage = request.getMortgages().stream().filter(m -> m.getInterestType().equals(InterestType.VARIABLE)).findAny();
            assertTrue(actualVariableMortgage.isPresent());

            //Check all database fields
            assertNotNull(actualFixedMortgage.get().getId());//Generated by DB
            assertNotNull(actualVariableMortgage.get().getId());//Generated by DB
            expectedFixedMortgage.setId(actualFixedMortgage.get().getId());
            expectedVariableMortgage.setId(actualVariableMortgage.get().getId());
            assertEquals(expectedFixedMortgage, actualFixedMortgage.get());
            assertEquals(expectedVariableMortgage, actualVariableMortgage.get());



        }
    }
}
