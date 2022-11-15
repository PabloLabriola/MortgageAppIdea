package com.cognizant.mortgagefeasibilityvalidator.infrastructure.broker.rabbitmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(RabbitmqConfigProperties.class)
@Profile("!integration")
public class RabbitmqConfig {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${rabbitmq.mortgage.confirmed.queue}")
    private String topicQueue;

    @Value("${rabbitmq.mortgage.confirmed.exchange}")
    private String topicExchange;

    @Value("${rabbitmq.mortgage.confirmed.binding.key}")
    private String bindingKey;


    @Bean
    public Queue createTopicQueue(){
        return new Queue(topicQueue, true, false, false);
    }
    @Bean
    public TopicExchange createTopicExchange(){
        return new TopicExchange(topicExchange, true, false);
    }
    @Bean
    public Binding createTopicBinding(){
        return BindingBuilder.bind(createTopicQueue()).to(createTopicExchange()).with(bindingKey);
    }
    @Bean
    public MessageConverter messageConverter(){
        ObjectMapper mapper= new ObjectMapper();
        return new Jackson2JsonMessageConverter(mapper);
    }
    @Bean
    public ConnectionFactory connectionFactory(RabbitmqConfigProperties rabbitmqConfigProperties){
        CachingConnectionFactory connectionFactory= new CachingConnectionFactory(rabbitmqConfigProperties.getHost());
        connectionFactory.setUsername(rabbitmqConfigProperties.getUsername());
        connectionFactory.setPassword(rabbitmqConfigProperties.getPassword());
        connectionFactory.setPort(rabbitmqConfigProperties.getPort());
        connectionFactory.setVirtualHost(rabbitmqConfigProperties.getVirtualHost());
        return connectionFactory;
    }
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate= new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
    @PostConstruct
    public void init(){
        amqpAdmin.declareQueue(createTopicQueue());
        amqpAdmin.declareExchange(createTopicExchange());
        amqpAdmin.declareBinding(createTopicBinding());
    }
}
