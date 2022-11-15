package com.cognizant.legalsystem.infrastructure.repository;

import com.cognizant.legalsystem.domain.Mortgage;
import com.cognizant.legalsystem.domain.MortgageRepository;
import com.cognizant.legalsystem.domain.exceptions.SaveMortgageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MortgageRepositoryImpl implements MortgageRepository {

    @Autowired
    MortgageJpaRepository mortgageJpaRepository;

    @Override
    public UUID saveMortgage(Mortgage mortgage) {
        if(mortgage==null){
            throw new SaveMortgageException("Error Mortgage Null");
        }
        return mortgageJpaRepository.save(mortgage).getId();
    }

    @Override
    public Optional<Mortgage> findMortgageById(UUID mortgageId){
        return mortgageJpaRepository.findById(mortgageId);
    }
}
