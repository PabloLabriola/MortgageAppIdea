package com.cognizant.feesmodule.eventcontroller;

import com.cognizant.feesmodule.model.dtos.FeeEventDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class FeesEventsProducer {

    private final KafkaTemplate<String, FeeEventDto> kafkaTemplate;

    @Value("${kafka.topic.producer.fee.events}")
    private String FEE_EVENTS_TOPIC;

    public FeesEventsProducer(KafkaTemplate<String, FeeEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendFeeEvent(FeeEventDto feeDto) {
        log.debug("#### Producing feeDto: " + feeDto.toString());
        this.kafkaTemplate.send(FEE_EVENTS_TOPIC, feeDto);
    }
}