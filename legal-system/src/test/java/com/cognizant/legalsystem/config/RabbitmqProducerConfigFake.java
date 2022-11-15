package com.cognizant.legalsystem.config;

import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;
import java.util.function.Supplier;

@Profile({"rabbit"})
@Slf4j
@AllArgsConstructor
@Configuration
public class RabbitmqProducerConfigFake {

    public static UUID expectedUUID= UUID.fromString("f83630ff-8d58-4862-9b7a-51e9501081ed");

    @Bean
    public Supplier<MortgageConfirmedEvent> eventProducer(){
        return () -> {
            return MortgageConfirmedEvent.builder().mortgageId(expectedUUID).build();
        };
    }
}
