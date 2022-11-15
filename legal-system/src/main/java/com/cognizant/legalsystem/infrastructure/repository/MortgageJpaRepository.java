package com.cognizant.legalsystem.infrastructure.repository;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MortgageJpaRepository extends JpaRepository<Mortgage, UUID> {



}
