package com.example.rabbitmq.config;

import com.example.rabbitmq.sender.RabbitSender;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {

    @Bean
    public Queue hello() {
        return new Queue("hello");
    }

}
