package com.cognizant.feesbatchreport.infrastructure;

import com.cognizant.feesbatchreport.model.dtos.FeeEventDto;
import com.cognizant.feesbatchreport.service.FeesBatchReportService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
@Log4j2
public class KafkaConsumer {

    private FeesBatchReportService service;

    private KafkaListenerEndpointRegistry listenerEndpointRegistry;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory",
            autoStartup = "false")
    public void receiveAllMessages(List<FeeEventDto> feeList) throws IOException {

        service.createReport(feeList);
        listenerEndpointRegistry.stop();

    }
}
