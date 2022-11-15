package com.cognizant.legalsystem.application.savemortgage;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveMortgageServiceImplUnitTest {

    @Mock
    private SaveMortgageServiceImpl saveMortgageService;

    MortgageConfirmedEvent mortgageConfirmedEvent;
    Mortgage mortgage;

    @BeforeEach
    void setUp(){
       MortgageConfirmedEvent.builder()
               // TODO requestId is missing!
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
    void shouldSaveMortgageOK(){
        saveMortgageService.saveMortgage(mortgageConfirmedEvent);

        verify(saveMortgageService, times(1)).saveMortgage(mortgageConfirmedEvent);
    }
}