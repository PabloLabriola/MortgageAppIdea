package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.InterestType;
import com.cognizant.mortgagefeasibilityvalidator.model.Mortgage;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageStatus;
import com.cognizant.mortgagefeasibilityvalidator.model.Request;
import com.cognizant.mortgagefeasibilityvalidator.repositories.MortgageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConfirmationServiceImplUnitTest {

    private ConfirmationService confirmationService;
    private Mortgage approvedFixedMortgage;
    private Mortgage approvedVariableMortgage;
    @Mock
    MortgageRepository mortgageRepository;

    @Mock
    ConfirmationSendQueueService confirmationSendQueueService;

    @BeforeEach
    void testInitialization() {

        confirmationService = new ConfirmationServiceImpl(mortgageRepository, confirmationSendQueueService);

        Request request = Request.builder().build();

        approvedFixedMortgage = Mortgage.builder()
                .id(UUID.fromString("4c4e31d9-d058-4433-ba4f-dd0d3244a5a3"))
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
                .id(UUID.fromString("556ecdbc-fd16-43c1-9fb5-c904236e5ee9"))
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

    }

    @Nested
    class CheckFeasibilityResponse {

        @Test
        void shouldReturnOkResponseWhenIDIsInTheCorrectFormatAndExistsInDatabaseAndIsApproved() {
            when(mortgageRepository.findById(any())).thenReturn(Optional.of(approvedFixedMortgage));
            ResponseEntity<?> response = confirmationService.generateConfirmationResponse(approvedFixedMortgage.getId().toString());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void shouldReturnBadRequestResponseWhenIDIsNotInTheCorrectFormat() {
            ResponseEntity<?> response = confirmationService.generateConfirmationResponse("100");
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void shouldReturnNotFoundResponseWhenIDIsInTheCorrectFormatAndItDoesNotExistInTheDatabase() {
            when(mortgageRepository.findById(any())).thenReturn(Optional.empty());
            ResponseEntity<?> response = confirmationService.generateConfirmationResponse("0a0a00a0-a000-0000-aa0a-aa0a0000a0a0");
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }

        @Test
        void shouldReturnConflictResponseWhenMortgageIsNotInStatusApproved() {
            approvedFixedMortgage.setState(MortgageStatus.CONFIRMED);
            when(mortgageRepository.findById(any())).thenReturn(Optional.of(approvedFixedMortgage));
            ResponseEntity<?> response = confirmationService.generateConfirmationResponse(approvedFixedMortgage.getId().toString());
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }

    }


}