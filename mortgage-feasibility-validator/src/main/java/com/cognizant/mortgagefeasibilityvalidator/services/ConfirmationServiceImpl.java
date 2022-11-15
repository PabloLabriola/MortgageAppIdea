package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.mappers.MortgageMapper;
import com.cognizant.mortgagefeasibilityvalidator.model.*;
import com.cognizant.mortgagefeasibilityvalidator.repositories.MortgageRepository;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class ConfirmationServiceImpl implements ConfirmationService {
    private final MortgageRepository mortgageRepository;
    private final ConfirmationSendQueueService confirmationSendQueueService;
    private final MortgageMapper mortgageMapper = Mappers.getMapper(MortgageMapper.class);

    public ConfirmationServiceImpl (MortgageRepository mortgageRepository, ConfirmationSendQueueService confirmationSendQueueService){
        this.mortgageRepository = mortgageRepository;
        this.confirmationSendQueueService = confirmationSendQueueService;
    }

    @Override
    public ResponseEntity<?> generateConfirmationResponse(String mortgageId) {
        UUID mortgageUUID;

        try { mortgageUUID = UUID.fromString(mortgageId);
        } catch (IllegalArgumentException e){
            log.info("Received a mortgage with an invalid UUID format.");
            return ResponseEntity.badRequest().body("The mortgage id does not follow the correct UUID format.");
        }

        Optional<Mortgage> mortgage = mortgageRepository.findById(mortgageUUID);

        if (mortgage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!mortgage.get().getState().equals(MortgageStatus.APPROVED)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The target mortgage is not in status approved, whereby it can not be confirmed.");
        }

        Request request = mortgage.get().getRequest();
        request.getMortgages().forEach(m -> {
            m.setState(MortgageStatus.CANCELLED);
            log.info("Mortgage " + m.getId() + " set to status " + MortgageStatus.CANCELLED.name());
        });
        mortgage.get().setState(MortgageStatus.CONFIRMED);
        log.info("Mortgage " + mortgage.get().getInterestType() +" set to status "  + MortgageStatus.CONFIRMED.name());
        mortgageRepository.saveAll(request.getMortgages());

        Mortgage m = mortgage.get();
        MortgageConfirmedEvent mortgageConfirmedEvent = mortgageMapper.mortgageToMortgageConfirmedEvent(m);
        confirmationSendQueueService.sendQueue(mortgageConfirmedEvent);

        return ResponseEntity.ok(new ConfirmationResponse(request.getMortgages()));
    }
}
