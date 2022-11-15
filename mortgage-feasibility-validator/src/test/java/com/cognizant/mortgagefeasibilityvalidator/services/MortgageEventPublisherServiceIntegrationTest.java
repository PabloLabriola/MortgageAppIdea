package com.cognizant.mortgagefeasibilityvalidator.services;

import com.cognizant.mortgagefeasibilityvalidator.config.KafkaConfigTest;
import com.cognizant.mortgagefeasibilityvalidator.config.MortgageEventsConsumer;
import com.cognizant.mortgagefeasibilityvalidator.model.MortgageDetailsRequestModel;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(classes = {KafkaConfigTest.class})
@ActiveProfiles({"integration","kafka"})
@DirtiesContext
@Log4j2
@EmbeddedKafka(partitions=1, topics = {"${kafka.topic.mortgage.events}"},
        brokerProperties={ "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class MortgageEventPublisherServiceIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private MortgageEventsConsumer consumer;

    @Autowired
    private KafkaListenerEndpointRegistry listenerEndpointRegistry;

    @Autowired
    private FeasibilityService feasibilityService;

    @MockBean
    private ConfirmationSendQueueService confirmationSendQueueService;

    @BeforeEach
    void setUp(){
        for(MessageListenerContainer messageListenerContainer:listenerEndpointRegistry.getListenerContainers()){
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());
        }
    }

    @ParameterizedTest
    @CsvSource({"99.9,0,1", "100.0,0,1", "100.1,99999,2", "100.1,100000,1"})
    public void shouldPublishMortgageValidatedEventWithStatusAccepted(BigDecimal monthlyIncomeParameter,
                                                                      BigDecimal homePriceParameter, int countEvents) throws InterruptedException {

        consumer.setCountLatch(countEvents);

        feasibilityService.generateFeasibilityResponse(MortgageDetailsRequestModel.builder()
                .company("CHOAM").country("Spain").city("Madrid").homePrice(homePriceParameter).downPayment(new BigDecimal(0))
                .loanLength(0).monthlyIncomes(monthlyIncomeParameter).build());

        consumer.getLatch().await(1000,TimeUnit.MILLISECONDS);
        assertThat(consumer.getLatch().getCount(), equalTo(0L));
    }


}
