package com.example.orderservice.producers;

import com.example.orderservice.contracts.EventContract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventProducer<T> {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;

    public EventProducer(KafkaProducer<String, String> producer) {
        this.producer = producer;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void dispatch(EventContract<T> event) {
        try {
            String messageEvent = this.objectMapper.writeValueAsString(event);
            ProducerRecord record = new ProducerRecord<>(event.getEventTopic(), event.getEventId(), messageEvent);
             this.producer.send(record, (metadata, exception) -> {
                 if (exception != null) {
                     log.error("Error while sending message to Kafka", exception);
                 }
             });
        } catch (Exception e) {
            log.error("Error while dispatching event", e);
        }
    }
}