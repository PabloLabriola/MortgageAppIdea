package com.cognizant.legalsystem.config;

import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Profile({"rabbit"})
public abstract class RabbitContainerBase {

    @Container
    protected static RabbitMQContainer container=
            new RabbitMQContainer(DockerImageName.parse("rabbitmq").withTag("3-management"))
                    .withExposedPorts(5672, 15672);

    static{
        container.start();
    }

    @DynamicPropertySource
    private static void configure(DynamicPropertyRegistry registry){
        registry.add("spring.rabbit.host", container::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
    }
}
