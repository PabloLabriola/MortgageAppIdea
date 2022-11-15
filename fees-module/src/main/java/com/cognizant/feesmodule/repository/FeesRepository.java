package com.cognizant.feesmodule.repository;

import com.cognizant.feesmodule.model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeesRepository extends JpaRepository<Fee, UUID> {

}
