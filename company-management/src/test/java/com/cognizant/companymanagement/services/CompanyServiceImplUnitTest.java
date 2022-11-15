package com.cognizant.companymanagement.services;

import com.cognizant.companymanagement.repositories.CompanyRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplUnitTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyServiceImpl;

    @Nested
    class IsSubscribedTests {
        @Test
        void shouldReturnTrue_WhenCompanyExists() {
            when(companyRepository.getCompanies()).thenReturn(Stream.of("FirstCompany", "SecondCompany"));
            assertTrue(companyServiceImpl.isSubscribed("SecondCompany"));
            verify(companyRepository, times(1)).getCompanies();
        }

        @Test
        void shouldReturnFalse_WhenCompanyDoesNotExists() {
            when(companyRepository.getCompanies()).thenReturn(Stream.of("FirstCompany", "SecondCompany"));
            assertFalse(companyServiceImpl.isSubscribed("ThirdCompany"));
            verify(companyRepository, times(1)).getCompanies();
        }

        @Test
        void shouldReturnFalse_WhenStreamEmpty() {
            when(companyRepository.getCompanies()).thenReturn(Stream.empty());
            assertFalse(companyServiceImpl.isSubscribed("ThirdCompany"));
            verify(companyRepository, times(1)).getCompanies();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "      "})
        void shouldReturnFalse_WhenCompanyEmptyOrBlank(String company) {
            assertFalse(companyServiceImpl.isSubscribed(""));
            verifyNoInteractions(companyRepository);
        }

        @Test
        void shouldReturnFalse_WhenCompanyNull() {
            assertFalse(companyServiceImpl.isSubscribed(null));
            verifyNoInteractions(companyRepository);
        }
    }
}