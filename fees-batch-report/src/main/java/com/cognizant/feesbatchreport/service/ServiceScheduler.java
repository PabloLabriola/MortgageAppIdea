package com.cognizant.feesbatchreport.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@AllArgsConstructor
public class ServiceScheduler {

    KafkaListenerEndpointRegistry listenerEndpointRegistry;

    @Scheduled(cron = "${report.cron.execution}")
    public void startReportCreation() {
        listenerEndpointRegistry.start();

    }
}
