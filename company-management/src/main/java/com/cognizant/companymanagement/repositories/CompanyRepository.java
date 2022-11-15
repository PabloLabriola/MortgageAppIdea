package com.cognizant.companymanagement.repositories;

import java.util.stream.Stream;

public interface CompanyRepository {
    Stream<String> getCompanies();
}
