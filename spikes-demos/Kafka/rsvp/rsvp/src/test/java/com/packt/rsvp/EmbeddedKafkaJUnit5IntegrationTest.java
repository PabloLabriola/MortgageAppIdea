package com.packt.rsvp;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions=1, topics = {"${test.mortgage.topic}", "${test.fees.topic}"} ,brokerProperties={ "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmbeddedKafkaJUnit5IntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    Producer<String, String> producer;

    DefaultKafkaConsumerFactory<String, String> consumerFactory;
    KafkaMessageListenerContainer<String, String> container;
    BlockingQueue<ConsumerRecord<String,String>> records;

    @Value("${test.mortgage.topic}")
    private String MORTGAGE_TOPIC;

    @Value("${test.fees.topic}")
    private String FEES_TOPIC;

    @BeforeEach
    void setUp(){

        // Producer
        Map<String, Object> producerConfigs= new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer= new DefaultKafkaProducerFactory<>(producerConfigs, new StringSerializer(), new StringSerializer()).createProducer();

        // Consumer
        Map<String, Object> consumerConfigs= new HashMap<>(KafkaTestUtils.consumerProps("consumer-group-1", "false", embeddedKafkaBroker));
        consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerFactory= new DefaultKafkaConsumerFactory<>(consumerConfigs, new StringDeserializer(), new StringDeserializer());

        ContainerProperties containerProperties= new ContainerProperties(MORTGAGE_TOPIC);
        container= new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records= new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String,String>)records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());

        // Consumer-2
        consumerConfigs= new HashMap<>(KafkaTestUtils.consumerProps("consumer-group-1", "false", embeddedKafkaBroker));
        consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerFactory= new DefaultKafkaConsumerFactory<>(consumerConfigs, new StringDeserializer(), new StringDeserializer());

        ContainerProperties containerProperties2= new ContainerProperties(FEES_TOPIC);
        container= new KafkaMessageListenerContainer<>(consumerFactory, containerProperties2);
        records= new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String,String>)records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSEndingToSimpleProducer_thenMessageReceived() throws InterruptedException {

        producer.send(new ProducerRecord<>(MORTGAGE_TOPIC, "mortgage-event", "me-125487"));
        producer.flush();

        ConsumerRecord<String,String> singleRecordMortgage= records.poll(100, TimeUnit.MILLISECONDS);

        assertThat(singleRecordMortgage, notNullValue());
        assertThat(singleRecordMortgage.key(), is("mortgage-event"));
        assertThat(singleRecordMortgage.value(), is("me-125487"));

        producer.send(new ProducerRecord<>(FEES_TOPIC, "fees-event", singleRecordMortgage.value() + "-fe"));
        producer.flush();

        ConsumerRecord<String,String> singleRecordFee= records2.poll(100, TimeUnit.MILLISECONDS);

        assertThat(singleRecordFee, notNullValue());
        assertThat(singleRecordFee.key(), is("fees-event"));
        assertThat(singleRecordFee.value(), is("me-125487-fe"));

    }

    @AfterEach
    void tearDown(){
        container.stop();
    }

}
