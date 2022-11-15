package com.cognizant.mortgagefeasibilityvalidator.infrastructure.broker.rabbitmq.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Profile("!integration")
public class RabbitmqConfigProperties {
    private String host;
    private int port;
    private String virtualHost;
    private String username;
    private String password;
}
