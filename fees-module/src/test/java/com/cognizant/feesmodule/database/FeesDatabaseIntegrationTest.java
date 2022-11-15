package com.cognizant.feesmodule.database;

import com.cognizant.feesmodule.model.Fee;
import com.cognizant.feesmodule.model.MortgageStatus;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import com.cognizant.feesmodule.repository.FeesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("integration")
public class FeesDatabaseIntegrationTest {
    @Autowired
    private FeesRepository feesRepository;

    @Test
    public void shouldSave() {
        UUID uuidMortgageId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Fee fee = Fee.builder()
                .feeCalculation(BigDecimal.valueOf(100000))
                .mortgageId(uuidMortgageId)
                .totalAmount(BigDecimal.valueOf(100000))
                .build();

        Fee feeReaded = feesRepository.save(fee);

        assertEquals(fee, feeReaded);

    }
}
