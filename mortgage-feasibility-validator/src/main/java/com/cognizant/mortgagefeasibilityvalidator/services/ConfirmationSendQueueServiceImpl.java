package com.cognizant.mortgagefeasibilityvalidator.services;


import com.cognizant.mortgagefeasibilityvalidator.model.MortgageConfirmedEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationSendQueueServiceImpl implements ConfirmationSendQueueService {

    private final AmqpTemplate amqpTemplate;

    private final TopicExchange topicExchange;

    private final String routingKey;

    public ConfirmationSendQueueServiceImpl(AmqpTemplate amqpTemplate, TopicExchange topicExchange, @Value("${rabbitmq.mortgage.confirmed.routing.key}") String routingKey) {
        this.amqpTemplate = amqpTemplate;
        this.topicExchange = topicExchange;
        this.routingKey = routingKey;
    }

    public void sendQueue(MortgageConfirmedEvent mortgageConfirmedEvent) {
        amqpTemplate.convertAndSend(topicExchange.getName(), routingKey, mortgageConfirmedEvent);
    }
}
