package com.example.rabbitmq.sender;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RabbitSender {

    private final RabbitTemplate template;
    private final FanoutExchange fanoutExchange;
    private final AtomicInteger dots = new AtomicInteger(0);
    private final AtomicInteger count = new AtomicInteger(0);

    public RabbitSender(RabbitTemplate template, FanoutExchange fanoutExchange) {
        this.template = template;
        this.fanoutExchange = fanoutExchange;
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {

        StringBuilder builder = new StringBuilder("Hello");

        if (dots.getAndIncrement() == 3) {
            dots.set(1);
        }

        for (int i = 0; i < dots.get(); i++) {
            builder.append('.');
        }

        builder.append(count.incrementAndGet());
        String message = builder.toString();
        template.convertAndSend(fanoutExchange.getName(), "", message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
