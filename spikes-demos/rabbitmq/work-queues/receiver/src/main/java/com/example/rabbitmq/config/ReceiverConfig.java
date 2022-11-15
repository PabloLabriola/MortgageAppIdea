package com.example.rabbitmq.config;

import com.example.rabbitmq.receiver.RabbitReceiver;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReceiverConfig {

    @Bean
    public Queue hello() {
        return new Queue("hello");
    }

    @Bean
    public RabbitReceiver receiver1() {
        return new RabbitReceiver(1);
    }

    @Bean
    public RabbitReceiver receiver2() {
        return new RabbitReceiver(2);
    }

}
