package com.cognizant.feesmodule.eventcontroller;

import com.cognizant.feesmodule.exceptions.FeeRepositoryException;
import com.cognizant.feesmodule.exceptions.NotAValidMortgageException;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import com.cognizant.feesmodule.services.FeesServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MortgageEventsConsumer {

    private final FeesServiceImpl feesServiceImpl;

    public MortgageEventsConsumer(FeesServiceImpl feesServiceImpl) {
        this.feesServiceImpl = feesServiceImpl;
    }

    @KafkaListener(topics = {"${kafka.topic.consumer.mortgage.events}"}, groupId = "${kafka.consumer.group-id}")
    public void receive(MortgageEventDto mortgageDto) throws NotAValidMortgageException, FeeRepositoryException {
        log.debug("#### Receiving and processing mortgageDto: " + mortgageDto.toString());
        feesServiceImpl.processFees(mortgageDto);
    }
}
