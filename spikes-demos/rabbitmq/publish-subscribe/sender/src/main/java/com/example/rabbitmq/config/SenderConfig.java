package com.example.rabbitmq.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SenderConfig {

    @Bean
    public Queue hello() {
        return new Queue("hello");
    }

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("tut.fanout");
    }

}
