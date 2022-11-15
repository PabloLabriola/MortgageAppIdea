package com.cognizant.mortgagefeasibilityvalidator.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Profile({"kafka"})
@Slf4j
public class MortgageEventsConsumer {

    private CountDownLatch latch;
    private ConsumerRecord payload;

    @KafkaListener(topics = {"${kafka.topic.mortgage.events}"}, groupId = "mortgage-groupId", id="mortgageContainer2")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        log.info("Message received: " + consumerRecord.toString());
        setPayload(consumerRecord);

        latch.countDown();
    }

    public ConsumerRecord getPayload() {
        return payload;
    }

    public void setPayload(ConsumerRecord payload) {
        this.payload = payload;
    }

    public void setCountLatch(int count){
        latch= new CountDownLatch(count);
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
