package com.cognizant.companymanagement.repositories;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class)
class CompanyRepositoryImplUnitTest {

    @Mock
    private Resource resource;

    @InjectMocks
    private CompanyRepositoryImpl companyRepository;

    @BeforeAll
    public static void setUp() throws IOException {
        Path FileWithContent = Files.createFile(Paths.get("src", "test", "resources", "file_with_content.dat").toAbsolutePath());
        BufferedWriter FileWithContentBuffer = Files.newBufferedWriter(FileWithContent);
        FileWithContentBuffer.write("FirstCompany\nSecondCompany");
        FileWithContentBuffer.close();

        Path emptyFile = Files.createFile(Paths.get("src", "test", "resources", "empty_file.dat").toAbsolutePath());
    }

    @AfterAll
    public static void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("src", "test", "resources", "file_with_content.dat").toAbsolutePath());
        Files.deleteIfExists(Paths.get("src", "test", "resources", "empty_file.dat").toAbsolutePath());
    }

    @Nested
    class getCompaniesTest {
        FileInputStream testFile;

        @Test
        void shouldReturnEmptyStream_WhenException() throws IOException {
            when(resource.getInputStream()).thenThrow(FileNotFoundException.class);
            assertEquals(0, companyRepository.getCompanies().count());
        }

        @Test
        void shouldReturnNonEmptyStream_WhenFileExistAndWithContent() throws IOException {
            testFile = new FileInputStream(Path.of("src", "test", "resources", "file_with_content.dat").toFile());
            when(resource.getInputStream()).thenReturn(testFile);
            assertEquals(2, companyRepository.getCompanies().count());
            testFile.close();
        }

        @Test
        void shouldReturnEmptyStream_WhenEmptyFileExists() throws IOException {
            testFile = new FileInputStream(Path.of("src", "test", "resources", "empty_file.dat").toFile());
            when(resource.getInputStream()).thenReturn(testFile);
            assertEquals(0, companyRepository.getCompanies().count());
            testFile.close();
        }
    }
}