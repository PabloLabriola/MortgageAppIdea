package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.exceptions.MortgageDetailsException;
import com.cognizant.mortgagefeasibilityvalidator.model.*;
import com.cognizant.mortgagefeasibilityvalidator.model.dtos.MortgageEventDto;
import com.cognizant.mortgagefeasibilityvalidator.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FeasibilityServiceImplUnitTest {

    @InjectMocks
    @Spy
    private FeasibilityServiceImpl feasibilityServiceImpl;

    private MortgageDetailsRequestModel mortgageDetailsRequestModel;
    private Mortgage fixedMortgage;
    private Mortgage variableMortgage;

    @Mock
    private SaveRequestService saveRequestService;

    @Mock
    private MortgageEventPublisherService mortgageEventPublisherService;

    private MortgageStatus status;
    private Request request;


    @BeforeEach
    void testInitialization() {

        BigDecimal monthlyIncomeThreshold = BigDecimal.valueOf(100L);
        BigDecimal incomePercentage = BigDecimal.valueOf(0.3);
        BigDecimal fixedRate = BigDecimal.valueOf(0.5);
        BigDecimal variableRate = BigDecimal.valueOf(1.2);

        feasibilityServiceImpl = new FeasibilityServiceImpl(saveRequestService, mortgageEventPublisherService);

        mortgageDetailsRequestModel = MortgageDetailsRequestModel
                .builder()
                .company("subscribed")
                .country("Spain")
                .city("Madrid")
                .homePrice(BigDecimal.valueOf(100000))
                .downPayment(BigDecimal.valueOf(1000))
                .loanLength(25)
                .monthlyIncomes(monthlyIncomeThreshold).build();

        request = Request.builder()
                .id(null)
                .company(mortgageDetailsRequestModel.getCompany())
                .build();

        fixedMortgage = Mortgage
                .builder()
                .interestType(InterestType.FIXED)
                .interestRate(fixedRate)
                .request(request)
                .state(MortgageStatus.APPROVED)
                .monthlyPayment(feasibilityServiceImpl.calculateMonthlyPayment(mortgageDetailsRequestModel.getMonthlyIncomes(), incomePercentage))
                .city("Madrid")
                .country("Spain")
                .downPayment(new BigDecimal(1000))
                .homePrice(new BigDecimal(100000))
                .loanLength(25)
                .monthlyIncomes(monthlyIncomeThreshold)
                .build();
        variableMortgage = Mortgage
                .builder()
                .interestType(InterestType.VARIABLE)
                .interestRate(variableRate)
                .request(request)
                .state(MortgageStatus.APPROVED)
                .monthlyPayment(feasibilityServiceImpl.calculateMonthlyPayment(mortgageDetailsRequestModel.getMonthlyIncomes(), incomePercentage))
                .city("Madrid")
                .country("Spain")
                .downPayment(new BigDecimal(1000))
                .homePrice(new BigDecimal(100000))
                .loanLength(25)
                .monthlyIncomes(monthlyIncomeThreshold)
                .build();

    }

    @Nested
    class CheckFeasibilityResponse {

        @ParameterizedTest
        @CsvSource({"0.001,0.001,1,0", "0.001,0.0,1,0", "0.001,-0.001,2,0", "-0.001,0.0,1,1", "-0.001,0.0,1,1"})
            // monthlyIncome, homePrice, countMortgages, expectedStatus
        void shouldReturnStatusVariableAndTypeVariable_WhenMonthlyIncomeVariableAndHomePriceVariable
        (double monthlyIncomeDiff, double homePriceDiff, int expectedCountMortgages, int expectedStatus) {

            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(100));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncomeDiff)));
            variableMortgage.setMonthlyIncomes(variableMortgage.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncomeDiff)));
            fixedMortgage.setMonthlyIncomes(fixedMortgage.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncomeDiff)));
            variableMortgage.setInterestRate(null);
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(homePriceDiff)));
            variableMortgage.setHomePrice(variableMortgage.getHomePrice().add(BigDecimal.valueOf(homePriceDiff)));
            fixedMortgage.setHomePrice(fixedMortgage.getHomePrice().add(BigDecimal.valueOf(homePriceDiff)));

            MortgageStatus status = feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            Request request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);

            fixedMortgage.setInterestRate(null);
            fixedMortgage.setState(MortgageStatus.values()[expectedStatus]);
            fixedMortgage.setState(MortgageStatus.values()[expectedStatus]);

            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            List<Mortgage> mortgageList;
            switch (expectedCountMortgages){
                case 1:
                    mortgageList= Collections.singletonList(fixedMortgage);
                    break;
                case 2:
                   mortgageList= Arrays.asList(fixedMortgage, variableMortgage);
                    break;
                default:
                    mortgageList= new ArrayList<>();
                    break;
            }

            assertEquals(new FeasibilityResponse(MortgageStatus.values()[expectedStatus], mortgageList), actual);
        }

        @Test
        void shouldReturnApprovedWithFixedType_WhenMonthlyIncomeHigherThanThresholdAndHomePriceHigherThanThreshold() {
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(10));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(10));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(0.00000000000000001)));
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            fixedMortgage.setInterestRate(null);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            fixedMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            fixedMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());


            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.APPROVED, Collections.singletonList(fixedMortgage)), actual);
        }

        @Test
        void shouldReturnApprovedWithFixedType_WhenMonthlyIncomeHigherThanThresholdAndHomePriceEqualThanThreshold() {
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(10));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            fixedMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            fixedMortgage.setInterestRate(null);
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));


            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.APPROVED, Collections.singletonList(fixedMortgage)), actual);
        }

        @Test
        void shouldReturnApprovedWithFixedAndVariableType_WhenMonthlyIncomeHigherThanThresholdAndHomePriceLessThanThreshold() {
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(10));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().subtract(BigDecimal.valueOf(0.00000000000000001)));
            fixedMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            fixedMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());
            fixedMortgage.setInterestRate(null);
            variableMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            variableMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());
            variableMortgage.setInterestRate(null);
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.APPROVED, Arrays.asList(fixedMortgage, variableMortgage)), actual);
        }

        @Test
        void shouldReturnRejected_WhenMonthlyIncomeLessThanThreshold() {
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(99999));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().subtract(BigDecimal.valueOf(0.00000000000000001)));
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(actual.getState(), MortgageStatus.REJECTED );
        }

        @Test
        void shouldReturnRejected_WhenMonthlyIncomeIsNull() {
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            mortgageDetailsRequestModel.setMonthlyIncomes(null);
            fixedMortgage.setMonthlyPayment(null);
            fixedMortgage.setState(MortgageStatus.REJECTED);
            fixedMortgage.setMonthlyIncomes(null);
            fixedMortgage.setInterestRate(null);
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.REJECTED, Collections.singletonList(fixedMortgage)), actual);
        }

        @Test
        void shouldThrowException_WhenHomePriceIsNull() {
            mortgageDetailsRequestModel.setHomePrice(null);
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));

            Throwable exception = assertThrows(MortgageDetailsException.class, ()->{
                feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);
            });

            assertEquals("HomePrice is null", exception.getMessage());
        }

        @Test
        void shouldThrowException_WhenMortgageDetailsRequestModelIsNull() {

            Throwable exception= assertThrows(MortgageDetailsException.class, ()->{
                feasibilityServiceImpl.generateFeasibilityResponse(null);
            });
            assertEquals("MortgageDetailsRequestModel null", exception.getMessage());
        }
    }


    @Nested
    class CheckMortgageStates {

        @ParameterizedTest
        @CsvSource({"0.001,0", "-0.001,1", "0.0,2"})
        // monthlyIncome, expectedStatus
        void shouldReturnStateVariable_WhenMonthlyIncomeVariable(double monthlyIncome, int expectedStatus) {
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(100));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncome)));
            MortgageStatus actual = feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            assertEquals(MortgageStatus.values()[expectedStatus], actual);
        }

        @Test
        void shouldReturnSateRejected_WhenHomePriceIsNull() {
            mortgageDetailsRequestModel.setHomePrice(null);
            MortgageStatus actual = feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            assertEquals(MortgageStatus.REJECTED, actual);
        }

    }

    @Nested
    class MortgageDetails {
        @ParameterizedTest
        @CsvSource({"0.001,0.001,0,1", "0.001,0.0,0,1", "0.001,-0.001,0,2"})
            // homePrice, expectedStatus, countMortgages
        void shouldReturnNumberVariableOfMortgages_WhenHomePriceVariableAndMonthlyIncomeHigherThan(double monthlyIncomeDiff, double homePriceDiff, int expectedStatus, int expectedCountMortgages) {

            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(100));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncomeDiff)));
            fixedMortgage.setMonthlyIncomes(fixedMortgage.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncomeDiff)));
            variableMortgage.setMonthlyIncomes(variableMortgage.getMonthlyIncomes().add(BigDecimal.valueOf(monthlyIncomeDiff)));

            variableMortgage.setInterestRate(null);
            fixedMortgage.setInterestRate(null);
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(homePriceDiff)));
            fixedMortgage.setHomePrice(fixedMortgage.getHomePrice().add(BigDecimal.valueOf(homePriceDiff)));
            variableMortgage.setHomePrice(variableMortgage.getHomePrice().add(BigDecimal.valueOf(homePriceDiff)));

            MortgageStatus status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            Request request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);

            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));
            List<Mortgage> expectedMortgageList;
            switch (expectedCountMortgages){
                case 1:
                    expectedMortgageList = Collections.singletonList(fixedMortgage);
                    break;
                case 2:
                    expectedMortgageList = Arrays.asList(fixedMortgage, variableMortgage);
                    break;
                default:
                    expectedMortgageList= new ArrayList<>();
            }

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.values()[expectedStatus], expectedMortgageList), actual);
            assertEquals(expectedCountMortgages, actual.getMortgages().size());
        }

        @Test
        void shouldReturnOneMortgages_WhenHomePriceHigherThanThreshold() {
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(100));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            variableMortgage.setInterestRate(null);
            fixedMortgage.setInterestRate(null);
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(0.00000000000000001)));
            fixedMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            fixedMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.APPROVED, Collections.singletonList(fixedMortgage)), actual);
            assertEquals(1, actual.getMortgages().size());
        }

        @Test
        void shouldReturnOneMortgages__WhenHomePriceIncomeEqualThanThreshold() {
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(100));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            variableMortgage.setInterestRate(null);
            fixedMortgage.setInterestRate(null);
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().add(BigDecimal.valueOf(0.00000000000000001)));
            fixedMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());
            fixedMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(1, actual.getMortgages().size());
        }

        @Test
        void shouldReturnTwoMortgages__WhenHomePriceLessThanThreshold() {
            setField(feasibilityServiceImpl, "monthlyIncomeThreshold", new BigDecimal(100));
            setField(feasibilityServiceImpl, "incomePercentage", new BigDecimal(0.3));
            setField(feasibilityServiceImpl, "homePriceThreshold", new BigDecimal(100000));
            variableMortgage.setInterestRate(null);
            fixedMortgage.setInterestRate(null);
            mortgageDetailsRequestModel.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes().add(BigDecimal.valueOf(0.00000000000000001)));
            mortgageDetailsRequestModel.setHomePrice(mortgageDetailsRequestModel.getHomePrice().subtract(BigDecimal.valueOf(0.00000000000000001)));
            fixedMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            fixedMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());
            variableMortgage.setMonthlyIncomes(mortgageDetailsRequestModel.getMonthlyIncomes());
            variableMortgage.setHomePrice(mortgageDetailsRequestModel.getHomePrice());
            status= feasibilityServiceImpl.checkFeasibilityStatus(mortgageDetailsRequestModel);
            request= feasibilityServiceImpl.createRequest(mortgageDetailsRequestModel, status);
            doReturn(request).when(saveRequestService).saveRequest(any(Request.class));
            doNothing().when(mortgageEventPublisherService).handleMortgageValidatedEvent(any(MortgageEventDto.class));

            FeasibilityResponse actual = feasibilityServiceImpl.generateFeasibilityResponse(mortgageDetailsRequestModel);

            assertEquals(new FeasibilityResponse(MortgageStatus.APPROVED, Arrays.asList(fixedMortgage, variableMortgage)), actual);
            assertEquals(2, actual.getMortgages().size());
        }
    }

    @Nested
    class CalculateMortgage {
        @Test
        void shouldReturnZero_WhenMonthlyIncomeIsZero() {
            BigDecimal actual = feasibilityServiceImpl.calculateMonthlyPayment(BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.3));
            assertEquals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_EVEN), actual);
        }

        @Test
        void shouldReturnPositive__WhenMonthlyIncomeIsPositive() {
            BigDecimal actual = feasibilityServiceImpl.calculateMonthlyPayment(BigDecimal.valueOf(0.1), BigDecimal.valueOf(0.3));
            assertEquals(BigDecimal.valueOf(0.03), actual);
        }

    }
}