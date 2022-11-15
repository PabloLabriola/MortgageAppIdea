package com.example.rabbitmq.sender;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RabbitSender {

    private final RabbitTemplate template;
    private final DirectExchange direct;
    private String[] keys = {"orange", "black", "green"};
    private AtomicInteger index = new AtomicInteger(0);
    private AtomicInteger count = new AtomicInteger(0);

    public RabbitSender(RabbitTemplate template, DirectExchange direct) {
        this.template = template;
        this.direct = direct;
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {

        StringBuilder builder = new StringBuilder("Hello to ");

        if (this.index.incrementAndGet() == 3) {
            this.index.set(0);
        }

        String key = keys[this.index.get()];

        builder.append(key).append(' ');
        builder.append(this.count.incrementAndGet());
        String message = builder.toString();

        template.convertAndSend(direct.getName(), key, message);

        System.out.println(" [x] Sent '" + message + "'");
    }
}
