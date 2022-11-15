package com.cognizant.mortgagefeasibilityvalidator.config;

import com.cognizant.mortgagefeasibilityvalidator.model.MortgageConfirmedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Log4j2
@Profile({"rabbit"})
public class RabbitConsumerFake {

    private CountDownLatch latch;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setCountLatch(int count){
        latch= new CountDownLatch(count);
    }

    @RabbitListener(
            bindings = @QueueBinding(
                    value= @Queue,
                    exchange = @Exchange(value= "topic-exchange-feasibility-legal", type = ExchangeTypes.TOPIC),
                    key = "mortgage.confirmed.key"
            )
    )
    public void consumeEvent(MortgageConfirmedEvent event){
        log.info("Event received: " + event.toString());
        latch.countDown();
    }
}
