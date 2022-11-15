package com.cognizant.legalsystem.infrastructure.repository;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.exceptions.SaveMortgageException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
class MortgageRepositoryImplUnitTest {

    @InjectMocks
    MortgageRepositoryImpl mortgageRepository;

    @Spy
    MortgageJpaRepository mortgageJpaRepository;

    @Test
    public void shouldMortgageRepositorySaveOK_whenMortgageOK(){
        when(mortgageJpaRepository.save(any(Mortgage.class))).thenReturn(new Mortgage());

        mortgageRepository.saveMortgage(new Mortgage());

        verify(mortgageJpaRepository, times(1)).save(any(Mortgage.class));
    }

    @Test
    public void shouldMortgageRepositoryThrowException_whenMortgageNull(){
        Throwable exception= assertThrows(SaveMortgageException.class, ()->mortgageRepository.saveMortgage(null));
        assertEquals(exception.getMessage(), "Error Mortgage Null");
    }

}