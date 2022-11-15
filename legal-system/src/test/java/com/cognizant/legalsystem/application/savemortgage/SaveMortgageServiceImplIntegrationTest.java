package com.cognizant.legalsystem.application.savemortgage;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import com.cognizant.legalsystem.domain.MortgageRepository;
import com.cognizant.legalsystem.infrastructure.repository.MortgageRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("integration")
class SaveMortgageServiceImplIntegrationTest {

    @Autowired
    private SaveMortgageService saveMortgageService;

    @Autowired
    @SpyBean
    private MortgageRepository mortgageRepository;

    private MortgageConfirmedEvent mortgageConfirmedEvent;

    @BeforeEach
    void setUp() {

        mortgageConfirmedEvent = MortgageConfirmedEvent.builder()
                .company("CHOAM")
                .country("USA")
                .city("Miami")
                .homePrice(new BigDecimal(100000))
                .downPayment(new BigDecimal(0))
                .loanLength(new BigDecimal(0))
                .monthlyIncomes(new BigDecimal(0))
                .interestRate(new BigDecimal(0))
                .monthlyPayment(new BigDecimal(0))
                .mortgageId(UUID.fromString("b40249e9-4e88-4ca3-8de2-399601513fdf"))
                .build();
    }

    @Test
    void shouldSaveMortgageOK_givenMortgageConfirmedEventOK(){

        saveMortgageService.saveMortgage(mortgageConfirmedEvent);

        verify(mortgageRepository, times(1)).saveMortgage(any(Mortgage.class));

    }
}