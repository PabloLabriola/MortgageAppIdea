package com.cognizant.companymanagement.repositories;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Log4j2
@Repository
public class CompanyRepositoryImpl implements CompanyRepository {

    @Value("classpath:${company.dataFile}")
    public Resource resource;

    /***
     * Get the list of companies stored in the resource file.
     * @return companyStream which a Stream that contains all the companies in the file.
     */
    @Override
    public Stream<String> getCompanies() {
        Stream<String> companyStream;
        try {
            log.debug("Getting list of companies from: " + resource.getURI());
            companyStream = new BufferedReader(new InputStreamReader(resource.getInputStream())).lines();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            companyStream = Stream.empty();
        }
        return companyStream;
    }
}
