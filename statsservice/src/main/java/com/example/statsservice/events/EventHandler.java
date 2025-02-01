package com.example.statsservice.events;

import com.example.statsservice.contracts.EventHandlerContract;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventHandler implements EventHandlerContract {

    private final ObjectMapper objectMapper;
    private final OrderCreatedHandler handler;

    public EventHandler(ObjectMapper objectMapper, OrderCreatedHandler handler) {
        this.handler = handler;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(ConsumerRecord<String, String> record) throws IOException {

        switch (record.topic()) {
            case "order.completed":
                OrderCompletedEvent event = this.objectMapper
                    .readValue(record.value(), OrderCompletedEvent.class);

                this.handler.updateOrCreateStats(event);
                break;

            default:
                throw new IOException(record.topic() + ": topic is not a valid topic");
        }
    }
}