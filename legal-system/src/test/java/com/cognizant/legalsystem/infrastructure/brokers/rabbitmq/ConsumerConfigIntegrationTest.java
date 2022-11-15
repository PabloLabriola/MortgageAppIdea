package com.cognizant.legalsystem.infrastructure.brokers.rabbitmq;

import com.cognizant.legalsystem.application.savemortgage.SaveMortgageService;
import com.cognizant.legalsystem.config.RabbitContainerBase;
import com.cognizant.legalsystem.config.RabbitmqProducerConfigFake;
import com.cognizant.legalsystem.domain.MortgageConfirmedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles({"integration","rabbit"})
@DirtiesContext
@ContextConfiguration
class ConsumerConfigIntegrationTest extends RabbitContainerBase {

    @SpyBean
    private SaveMortgageService saveMortgageService;

    @Captor
    private ArgumentCaptor<MortgageConfirmedEvent> captor;


    @Test
    void shouldExecuteSaveMortgageOnce_whenMortgageConfirmedEventConsumedOK() throws InterruptedException {
        verify(saveMortgageService, times(1)).saveMortgage(captor.capture());
        assertThat(captor.getValue().getMortgageId()).isEqualTo(RabbitmqProducerConfigFake.expectedUUID);
    }
}