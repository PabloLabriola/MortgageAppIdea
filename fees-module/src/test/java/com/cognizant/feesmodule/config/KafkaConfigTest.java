package com.cognizant.feesmodule.config;

import com.cognizant.feesmodule.model.dtos.MortgageEventDto;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration
@ComponentScan("com.cognizant.feesmodule")
public class KafkaConfigTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Bean
    public Map<String, Object> producerConfigs() {
        return new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
    }

    @Bean
    public ProducerFactory<String, MortgageEventDto> producerFactory() {
        return new DefaultKafkaProducerFactory(producerConfigs(), new StringSerializer(), new JsonSerializer<MortgageEventDto>());
    }

    @Bean
    public KafkaTemplate<String, MortgageEventDto> kafkaTemplate(){
        return new KafkaTemplate<String, MortgageEventDto>(producerFactory());
    }


}