package com.packt.rsvp;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions=1, brokerProperties={ "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmbeddedKafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    Producer<String, String> producer;
    Consumer<String, String> consumer;

    @Value("${test.topic}")
    private String TOPIC;

    @BeforeEach
    void setUp(){
        // Producer
        Map<String, Object> producerConfigs= new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer= new DefaultKafkaProducerFactory<>(producerConfigs, new StringSerializer(), new StringSerializer()).createProducer();

        // Consumer
        Map<String, Object> consumerConfigs= new HashMap<>(KafkaTestUtils.consumerProps("consumer-group-1", "false", embeddedKafkaBroker));
        consumerConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer= new DefaultKafkaConsumerFactory<>(consumerConfigs, new StringDeserializer(), new StringDeserializer()).createConsumer();
    }

    @Test
    public void givenEmbeddedKafkaBroker_whenSEndingToSimpleProducer_thenMessageReceived(){

        producer.send(new ProducerRecord<>(TOPIC, "aggregate-id", "id-125487"));
        producer.flush();

        consumer.subscribe(Collections.singleton(TOPIC));
        consumer.poll(Duration.ZERO);

        ConsumerRecord<String, String> singleRecord= KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        assertThat(singleRecord, notNullValue());
        assertThat(singleRecord.key(), is("aggregate-id"));
        assertThat(singleRecord.value(), is("id-125487"));
    }

}
