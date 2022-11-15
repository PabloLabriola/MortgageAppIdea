package com.packt.rsvp.producer.handler;

import com.packt.rsvp.producer.RsvpKafkaProducer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.logging.Logger;

@Component
public class RsvpsWebSocketHandler extends AbstractWebSocketHandler {

    private static Logger logger= Logger.getLogger(RsvpsWebSocketHandler.class.getName());

    private final RsvpKafkaProducer producer;

    public RsvpsWebSocketHandler(RsvpKafkaProducer producer) {
        this.producer = producer;
    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        producer.sendRsvpMessage(message);
    }
}
