package com.example.productservice.consumers;

import com.example.productservice.contracts.EventHandlerContract;
import com.example.productservice.events.EventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;

@Slf4j
@Service
public class EventConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final EventHandlerContract eventHandler;

    public EventConsumer(KafkaConsumer<String, String> consumer, EventHandlerContract eventHandler) {
        this.consumer = consumer;
        this.consumer.subscribe(Arrays.asList("order.completed"));
        this.eventHandler = eventHandler;
    }

    @PostConstruct
    public void consume() {
        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        this.eventHandler.process(record);
                    }
                }
            } catch (Exception e) {
                log.error("Error while consuming", e);
            } finally {
                this.consumer.close();
            }
        });

        consumerThread.start();
    }
}