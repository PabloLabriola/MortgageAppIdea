package com.cognizant.companymanagement.services;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@ActiveProfiles("integration")
@SpringBootTest(properties = {"company.dataFile=Testcompanies.txt"})
class CompanyServiceImplIntegrationTest {

    @Autowired
    private CompanyService companyService;

    @BeforeAll
    public static void setUp() throws IOException {
        Files.deleteIfExists(Paths.get("src", "test", "resources", "Testcompanies.txt").toAbsolutePath());
        Path FileWithContent = Files.createFile(Paths.get("src", "test", "resources", "Testcompanies.txt").toAbsolutePath());
        BufferedWriter FileWithContentBuffer = Files.newBufferedWriter(FileWithContent);
        FileWithContentBuffer.write("FirstCompany\nSecondCompany");
        FileWithContentBuffer.close();
    }

    @Nested
    class IsSubscribed {
        @ParameterizedTest
        @CsvSource(value = {"FirstCompany", "SecondCompany"})
        void shouldReturnTrue_WhenValidCompanyPassed(String company) {
            assertTrue(companyService.isSubscribed(company));
        }

        @ParameterizedTest
        @ValueSource(strings = {"    ", "NonExistingCompnay", ""})
        void shouldReturnFalse_WhenNonValidCompany(String company) {
            assertFalse(companyService.isSubscribed(company));
        }
    }
}