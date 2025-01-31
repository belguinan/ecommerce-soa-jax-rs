package com.example.productservice.events;

import com.example.productservice.contracts.EventHandlerContract;
import com.example.productservice.services.ProductService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EventHandler implements EventHandlerContract {

    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public EventHandler(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void process(ConsumerRecord<String, String> record) throws IOException {
        switch (record.topic()) {
            case "order.completed":
                OrderCompletedEvent event = this.objectMapper
                    .readValue(record.value(), OrderCompletedEvent.class);

                event.getItems().forEach(item ->
                    this.productService.updateStock(
                        item.getProductId(),
                        item.getQuantity()
                    )
                );
                break;

            default:
                throw new IOException(record.topic() + ": topic is not a valid topic");
        }
    }
}