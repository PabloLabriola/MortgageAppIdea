package com.example.rabbitmq.client;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RabbitClient {

    private final RabbitTemplate template;
    private final DirectExchange exchange;

    private int start = 0;

    public RabbitClient(RabbitTemplate template, DirectExchange exchange) {
        this.template = template;
        this.exchange = exchange;
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        System.out.println(" [x] Requesting fib(" + start + ")");

        Integer response = (Integer) template.convertSendAndReceive
                (exchange.getName(), "rpc", start++);

        System.out.println(" [.] Got '" + response + "'");
    }

}
