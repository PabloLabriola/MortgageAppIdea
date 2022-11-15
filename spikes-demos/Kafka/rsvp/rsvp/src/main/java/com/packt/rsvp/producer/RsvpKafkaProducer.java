package com.packt.rsvp.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;

import java.util.Properties;
import java.util.logging.Logger;

@Component
public class RsvpKafkaProducer {

    private static Logger logger= Logger.getLogger(RsvpKafkaProducer.class.getName());

    private final String MEETUP_TOPIC= "meetupTopic";
    private KafkaProducer<String, String> producer;

    public RsvpKafkaProducer(
            final @Value("${spring.kafka.producer.bootstrap-servers}") String bootstrapServers){
        Properties properties= new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<String, String>(properties);
    }

    public void sendRsvpMessage(WebSocketMessage<?> message){
        logger.info("Sending message:\\n" + message.getPayload());

        ProducerRecord<String, String> record= new ProducerRecord<>(MEETUP_TOPIC, message.getPayload().toString());
        producer.send(record);
        producer.flush();
        //producer.close();
    }

}
