package com.cognizant.feesmodule.eventcontroller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;
import java.util.concurrent.CountDownLatch;

@Log4j2
@Component
public class MortgageEventsConsumerTest {

    @Autowired
    private KafkaListenerEndpointRegistry listenerEndpointRegistry;
    private CountDownLatch latch;
    private ConsumerRecord payload;

    @KafkaListener(topics = {"${kafka.topic.producer.fee.events}"}, groupId = "mortgage-groupId", id="mortgageContainer2")
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
