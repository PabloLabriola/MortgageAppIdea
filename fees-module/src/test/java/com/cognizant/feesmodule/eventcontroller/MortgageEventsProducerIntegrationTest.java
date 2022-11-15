package com.cognizant.feesmodule.eventcontroller;

import com.cognizant.feesmodule.exceptions.FeeRepositoryException;
import com.cognizant.feesmodule.exceptions.NotAValidMortgageException;
import com.cognizant.feesmodule.model.Fee;
import com.cognizant.feesmodule.model.MortgageStatus;
import com.cognizant.feesmodule.model.dtos.FeeEventDto;
import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import com.cognizant.feesmodule.repository.FeesRepository;
import com.cognizant.feesmodule.services.FeesService;
import com.cognizant.feesmodule.services.FeesServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Log4j2
@SpringBootTest
@ActiveProfiles({"integration"})
@DirtiesContext
@EmbeddedKafka(partitions=1, topics = {"${kafka.topic.producer.fee.events}"}, brokerProperties={ "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class MortgageEventsProducerIntegrationTest {
    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaListenerEndpointRegistry listenerEndpointRegistry;

    @Autowired
    private FeesServiceImpl feesServiceImpl;

    @Autowired
    private MortgageEventsConsumerTest mortgageEventsConsumerTest;

    @MockBean
    private MortgageEventsConsumer mortgageEventsConsumer;

    @MockBean
    private FeesRepository feesRepository;


    @BeforeEach
    void setUp(){
        for(MessageListenerContainer messageListenerContainer:listenerEndpointRegistry.getListenerContainers()){
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @Test
    public void shouldPublishOkFeeEventDto() {
        Fee fee = Fee.builder().mortgageId(UUID.randomUUID()).feeCalculation(new BigDecimal(1)).
                id(UUID.randomUUID()).totalAmount(new BigDecimal(2)).build();
        FeeEventDto feeDto = FeeEventDto.builder().build();

        doReturn(fee).when(feesRepository).save(any());

        MortgageEventDto mortgageEventDto = MortgageEventDto.builder()
                .company("CHOAM")
                .mortgageId(UUID.fromString("e40fa8e9-effc-4eff-9670-04b2ca17e050"))
                .homePrice(BigDecimal.valueOf(100000))
                .mortgageStatus(MortgageStatus.APPROVED)
                .build();

        mortgageEventsConsumerTest.setCountLatch(1);

        try {
            feesServiceImpl.processFees(mortgageEventDto);
        } catch (NotAValidMortgageException e) {
            log.error(e);
        } catch (FeeRepositoryException e) {
            log.error(e);
        }

        try {
            mortgageEventsConsumerTest.getLatch().await(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error(e);
        }

        assertEquals(mortgageEventsConsumerTest.getLatch().getCount(), 0L);
    }
}
