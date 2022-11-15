package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.*;
import com.cognizant.mortgagefeasibilityvalidator.repositories.MortgageRepository;
import com.cognizant.mortgagefeasibilityvalidator.repositories.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@ActiveProfiles({"integration"})
class ConfirmationServiceImplIntegrationTest {

    private ConfirmationService confirmationService;

    @Autowired
    private MortgageRepository mortgageRepository;

    @Autowired
    private RequestRepository requestRepository;

    @MockBean
    private ConfirmationSendQueueService confirmationSendQueueService;

    private Mortgage approvedFixedMortgage;
    private Mortgage approvedVariableMortgage;

    @Nested
    class CheckConfirmation {

        @BeforeEach
        void testInitialization() {

            Request request = Request.builder().build();

            approvedFixedMortgage = Mortgage.builder()
                    .interestType(InterestType.FIXED)
                    .interestRate(new BigDecimal("1.2"))
                    .monthlyPayment(new BigDecimal("30.03"))
                    .request(request)
                    .state(MortgageStatus.APPROVED)
                    .city("Valencia")
                    .country("Spain")
                    .downPayment(new BigDecimal(23000))
                    .homePrice(new BigDecimal(260000))
                    .loanLength(60)
                    .monthlyIncomes(new BigDecimal(2000)).build();

            approvedVariableMortgage = Mortgage.builder()
                    .interestType(InterestType.VARIABLE)
                    .interestRate(new BigDecimal("0.5"))
                    .monthlyPayment(new BigDecimal("30.03"))
                    .request(request)
                    .state(MortgageStatus.APPROVED)
                    .city("Valencia")
                    .country("Spain")
                    .downPayment(new BigDecimal(23000))
                    .homePrice(new BigDecimal(260000))
                    .loanLength(60)
                    .monthlyIncomes(new BigDecimal(2000)).build();

            request.setMortgages(Arrays.asList(approvedFixedMortgage, approvedVariableMortgage));

            requestRepository.save(request);

            confirmationService = new ConfirmationServiceImpl(mortgageRepository, confirmationSendQueueService);

            confirmationService.generateConfirmationResponse(approvedFixedMortgage.getId().toString());
        }

        @Test
        void shouldChangeApprovedToConfirmedAndCancelledWhenIDIsInTheCorrectFormatAndExistsInDatabaseAndIsApproved() {

            Optional<Mortgage> actualFixedMortgage = mortgageRepository.findById(approvedFixedMortgage.getId());
            Optional<Mortgage> actualVariableMortgage = mortgageRepository.findById(approvedVariableMortgage.getId());

            assertTrue(actualFixedMortgage.isPresent());
            assertTrue(actualVariableMortgage.isPresent());

            assertEquals(MortgageStatus.CONFIRMED, actualFixedMortgage.get().getState());
            assertEquals(MortgageStatus.CANCELLED, actualVariableMortgage.get().getState());

        }

        @Test
        void shouldNotChangeCancelledToConfirmedWhenConfirmingCancelledMortgage() {
            confirmationService.generateConfirmationResponse(approvedVariableMortgage.getId().toString()); // 409 Conflict

            Optional<Mortgage> actualFixedMortgage = mortgageRepository.findById(approvedFixedMortgage.getId());
            Optional<Mortgage> actualVariableMortgage = mortgageRepository.findById(approvedVariableMortgage.getId());

            assertTrue(actualFixedMortgage.isPresent());
            assertTrue(actualVariableMortgage.isPresent());

            assertEquals(MortgageStatus.CONFIRMED, actualFixedMortgage.get().getState());
            assertEquals(MortgageStatus.CANCELLED, actualVariableMortgage.get().getState());

        }


    }
}
