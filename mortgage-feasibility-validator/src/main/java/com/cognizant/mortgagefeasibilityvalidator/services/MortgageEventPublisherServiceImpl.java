package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.model.dtos.MortgageEventDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MortgageEventPublisherServiceImpl implements MortgageEventPublisherService {

    private final KafkaTemplate<String, MortgageEventDto> kafkaTemplate;

    @Value("${kafka.topic.mortgage.events}")
    private String MORTGAGE_EVENTS_TOPIC;

    public MortgageEventPublisherServiceImpl(KafkaTemplate<String, MortgageEventDto> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void handleMortgageValidatedEvent(MortgageEventDto mortgageValidatedEvent) {
        kafkaTemplate.send(
                new ProducerRecord<>(MORTGAGE_EVENTS_TOPIC, null, mortgageValidatedEvent)
        );
    }
}
