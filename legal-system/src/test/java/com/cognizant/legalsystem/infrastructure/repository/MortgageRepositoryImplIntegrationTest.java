package com.cognizant.legalsystem.infrastructure.repository;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageRepository;
import com.cognizant.legalsystem.domain.exceptions.SaveMortgageException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles({"integration"})
@SpringBootTest
class MortgageRepositoryImplIntegrationTest {

    @Autowired
    MortgageRepository mortgageRepository;

    @Autowired
    MortgageJpaRepository mortgageJpaRepository;

    @Test
    public void shouldMortgageRepositorySaveOK_whenMortgageOK(){
        UUID mortgageId= mortgageRepository.saveMortgage(new Mortgage());
        Optional<Mortgage> optionalMortgage= mortgageRepository.findMortgageById(mortgageId);

        assertTrue(optionalMortgage.isPresent());
    }

    @Test
    public void shouldMortgageRepositoryThrowException_whenMortgageNull(){
        Throwable exception= assertThrows(SaveMortgageException.class, ()->mortgageRepository.saveMortgage(null));
        assertEquals(exception.getMessage(), "Error Mortgage Null");
    }
}