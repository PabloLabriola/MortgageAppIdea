package com.cognizant.mortgagefeasibilityvalidator.infrastructure.broker.rabbitmq.sender;

import com.cognizant.mortgagefeasibilityvalidator.model.MortgageConfirmedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


@Component
@Profile("!integration")
@Log4j2
public class RabbitSender {

    private final AmqpTemplate amqpTemplate;
    private final Queue queue;

    public RabbitSender(AmqpTemplate amqpTemplate, Queue queue) {
        this.amqpTemplate = amqpTemplate;
        this.queue = queue;
    }

    public void send(MortgageConfirmedEvent mortgageConfirmedEvent) {
         try(ByteArrayOutputStream mortgageConfirmedByteArrayOutputStream = new ByteArrayOutputStream()){
            try(ObjectOutputStream mortgageConfirmedObjectOutputStream = new ObjectOutputStream(mortgageConfirmedByteArrayOutputStream)) {
                mortgageConfirmedObjectOutputStream.writeObject(mortgageConfirmedEvent);
            }
            byte[] mortgageConfirmedByteArray = mortgageConfirmedByteArrayOutputStream.toByteArray();
            Message message = new Message(mortgageConfirmedByteArray);
          this.amqpTemplate.send(queue.getName(), message);
           log.info("Sent to rabbitmq queue the following message '" + message + "'");
         } catch (IOException e) {
             log.error("Error sending confirmed mortgage to rabbitmq.");
             e.printStackTrace();
        }
    }
}
