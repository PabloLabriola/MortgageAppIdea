package com.cognizant.feesbatchreport;

import com.cognizant.feesbatchreport.infrastructure.KafkaConsumer;
import com.cognizant.feesbatchreport.model.dtos.FeeEventDto;
import com.cognizant.feesbatchreport.service.FeesBatchReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;


@SpringBootTest
@DirtiesContext
@ActiveProfiles("integration")
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" }, topics = {"FEES_EVENTS"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaIntegrationTests {

    private Producer<String, String> producer;

    @Autowired
    private KafkaListenerEndpointRegistry listenerEndpointRegistry;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @SpyBean
    private KafkaConsumer consumer;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<List<FeeEventDto>> listCaptor;

    @MockBean
    FeesBatchReportService service;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();
    }

    @Test
    public void shouldReceiveListOfMortgageFeeDTO() throws InterruptedException, IOException {

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.springframework.kafka.support.serializer.JsonSerializer");

        Producer<String, FeeEventDto> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<>(topic, "key-1", new FeeEventDto("My_Company", UUID.randomUUID(), new BigDecimal("10.20"), new BigDecimal("20000.50"))));
        producer.send(new ProducerRecord<>(topic, "key-2", new FeeEventDto("My_Company", UUID.randomUUID(), new BigDecimal("20.30"), new BigDecimal("20000.50"))));
        producer.send(new ProducerRecord<>(topic, "key-3", new FeeEventDto("My_Company", UUID.randomUUID(), new BigDecimal("30.40"), new BigDecimal("20000.50"))));

        producer.close();

        listenerEndpointRegistry.start();
        Thread.sleep(3000);
        Mockito.verify(service).createReport(listCaptor.capture());
        List<FeeEventDto> listDto = listCaptor.getValue();

        assertEquals(3, listDto.size());

    }

    @AfterAll
    void shutdown() {
        producer.close();
    }
}
