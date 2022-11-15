package com.cognizant.feesmodule.eventcontroller;

import com.cognizant.feesmodule.config.KafkaConfigTest;
import com.cognizant.feesmodule.exceptions.FeeRepositoryException;
import com.cognizant.feesmodule.exceptions.NotAValidMortgageException;
import com.cognizant.feesmodule.model.MortgageStatus;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import com.cognizant.feesmodule.services.FeesServiceImpl;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {KafkaConfigTest.class})
@ActiveProfiles({"integration"})
@DirtiesContext
@EmbeddedKafka(partitions=1, topics = {"${kafka.topic.consumer.mortgage.events}"}, brokerProperties={ "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class MortgageEventsConsumerTestIntegrationTest {

    @MockBean
    FeesEventsProducer feesEventsProducer;

    @MockBean
    private FeesServiceImpl feesServiceImpl;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaListenerEndpointRegistry listenerEndpointRegistry;

    @Autowired
    KafkaTemplate<String, MortgageEventDto> kafkaTemplate;

    @Value("${kafka.topic.consumer.mortgage.events}")
    private String MORTGAGE_EVENT_TOPIC;


    @BeforeEach
    void setUp(){
        for(MessageListenerContainer messageListenerContainer:listenerEndpointRegistry.getListenerContainers()){
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @Test
    void shouldExecuteProcessFeesOnce_whenOneMortgageEventConsumedOK() throws NotAValidMortgageException, FeeRepositoryException, InterruptedException {
        kafkaTemplate.send(new ProducerRecord<>(
                MORTGAGE_EVENT_TOPIC,
                null,
                MortgageEventDto.builder()
                        .company("CHOAM")
                        .mortgageId(UUID.fromString("e40fa8e9-effc-4eff-9670-04b2ca17e050"))
                        .homePrice(BigDecimal.valueOf(100000))
                        .mortgageStatus(MortgageStatus.APPROVED)
                        .build())
        );

        Thread.sleep(3000);

        verify(feesServiceImpl, times(1)).processFees(any(MortgageEventDto.class));
    }
}