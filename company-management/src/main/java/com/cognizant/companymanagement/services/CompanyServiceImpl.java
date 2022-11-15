package com.cognizant.companymanagement.services;

import com.cognizant.companymanagement.repositories.CompanyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /***
     * Verifies if a given company is subscribed or not.
     * @param company the name of company to check the subscription for.
     * @return boolean to reflect if the company is subscribed or not.
     */
    public boolean isSubscribed(String company) {

        log.debug("Checking subscription of company " + company);

        if (company != null && !company.isBlank() && !company.isEmpty())
            return companyRepository.getCompanies().anyMatch(c -> c.equals(company));

        return false;
    }
}
