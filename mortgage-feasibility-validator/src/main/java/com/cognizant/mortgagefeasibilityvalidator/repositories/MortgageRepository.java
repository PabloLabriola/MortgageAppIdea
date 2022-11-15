package com.cognizant.mortgagefeasibilityvalidator.repositories;

import com.cognizant.mortgagefeasibilityvalidator.model.Mortgage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MortgageRepository extends JpaRepository<Mortgage, UUID> {

}
