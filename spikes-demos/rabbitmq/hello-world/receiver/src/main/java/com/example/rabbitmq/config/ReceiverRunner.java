package com.example.rabbitmq.config;

import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Used to shut application off after specified time has elapsed.
 */
@Setter
@Component
@ConfigurationProperties("tutorial.receiver")
public class ReceiverRunner implements CommandLineRunner {

    private int duration;
    private final ConfigurableApplicationContext ctx;

    public ReceiverRunner(ConfigurableApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run(String... arg0) throws Exception {
        System.out.println("Ready ... running for " + duration + "ms");
        Thread.sleep(duration);
        ctx.close();
    }
}
