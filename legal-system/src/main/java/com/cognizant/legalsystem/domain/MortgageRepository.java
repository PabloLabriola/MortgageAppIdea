package com.cognizant.legalsystem.domain;

import java.util.Optional;
import java.util.UUID;

public interface MortgageRepository {
    public UUID saveMortgage(Mortgage mortgage);

    public Optional<Mortgage> findMortgageById(UUID mortgageId);
}