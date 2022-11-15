package com.cognizant.legalsystem.infrastructure.brokers.rabbitmq;

import com.cognizant.legalsystem.application.savemortgage.SaveMortgageService;
import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import com.cognizant.legalsystem.domain.exceptions.MortgageEventException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
@Configuration
public class RabbitmqConsumerConfig {

    @Autowired
    private SaveMortgageService saveMortgageService;

    @Bean
    public MessageConverter messageConverter(){
        ObjectMapper mapper= new ObjectMapper();
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public Consumer<MortgageConfirmedEvent> eventConsumer(){
        return mortgageConfirmedEvent -> {
            log.info("Receiving Rabbit message(MortgageConfirmedEvent): "+ mortgageConfirmedEvent.getMortgageId().toString());

            saveMortgageService.saveMortgage(mortgageConfirmedEvent);
        };
    }


}
