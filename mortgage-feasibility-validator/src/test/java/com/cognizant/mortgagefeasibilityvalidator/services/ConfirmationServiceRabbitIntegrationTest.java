
package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.config.RabbitConsumerFake;
import com.cognizant.mortgagefeasibilityvalidator.config.RabbitContainerBase;
import com.cognizant.mortgagefeasibilityvalidator.model.InterestType;
import com.cognizant.mortgagefeasibilityvalidator.model.Mortgage;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageStatus;
import com.cognizant.mortgagefeasibilityvalidator.model.Request;
import com.cognizant.mortgagefeasibilityvalidator.repositories.MortgageRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles({"integration", "rabbit"})
@Testcontainers
@DirtiesContext
class ConfirmationServiceRabbitIntegrationTest extends RabbitContainerBase {

    @Autowired
    private ConfirmationService confirmationService;

    @MockBean
    private MortgageRepository mortgageRepository;

    @Autowired
    private RabbitConsumerFake rabbitConsumerFake;

    private final BigDecimal fixedRate;

    private final BigDecimal monthlyIncomeThreshold;

    ConfirmationServiceRabbitIntegrationTest(@Value("${InterestRate.Fixed}") BigDecimal fixedRate,
                                             @Value("${MonthlyIncome.Threshold}") BigDecimal monthlyIncomeThreshold) {
        this.fixedRate = fixedRate;
        this.monthlyIncomeThreshold = monthlyIncomeThreshold;
    }


    @Test
    void shouldSendMortgageConfirmedEventOK_whenWeConfirmMortgage() throws InterruptedException {
        Request request = Request.builder().build();
        Mortgage mortgage = Mortgage
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
                .request(request)
                .build();
        request.setMortgages(Arrays.asList(mortgage));

        Optional<Mortgage> optionalMortgage = Optional.of(mortgage);

        String UUIDstring = "f950072d-7ec4-4928-9ee7-7e9dadcbf49b";
        UUID mortgageUUID = UUID.fromString(UUIDstring);
        doReturn(optionalMortgage).when(mortgageRepository).findById(mortgageUUID);
        rabbitConsumerFake.setCountLatch(1);

        confirmationService.generateConfirmationResponse(UUIDstring);
        Thread.sleep(3000);
        rabbitConsumerFake.getLatch().await(1000, TimeUnit.MILLISECONDS);

        MatcherAssert.assertThat(rabbitConsumerFake.getLatch().getCount(), equalTo(0L));
    }
}