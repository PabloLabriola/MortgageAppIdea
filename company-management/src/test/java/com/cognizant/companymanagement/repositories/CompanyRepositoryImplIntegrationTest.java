package com.cognizant.companymanagement.repositories;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
@ActiveProfiles("integration")
@SpringBootTest(properties = {"company.dataFile=Testcompanies.txt"})
public class CompanyRepositoryImplIntegrationTest {

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeAll
    public static void setUp() throws IOException {
        Files.deleteIfExists(Paths.get("src", "test", "resources", "Testcompanies.txt").toAbsolutePath());
        Path FileWithContent = Files.createFile(Paths.get("src", "test", "resources", "Testcompanies.txt").toAbsolutePath());
        BufferedWriter FileWithContentBuffer = Files.newBufferedWriter(FileWithContent);
        FileWithContentBuffer.write("FirstCompany\nSecondCompany");
        FileWithContentBuffer.close();
    }

    @Nested
    class getCompanies {
        @Test
        void shouldReturnStreamOf2_WhenPickedUpCorrectFile() throws IOException {
            assertEquals(2, companyRepository.getCompanies().count());
        }
    }
}
