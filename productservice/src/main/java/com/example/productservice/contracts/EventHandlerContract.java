package com.example.productservice.contracts;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public interface EventHandlerContract {

    public void process(ConsumerRecord<String, String> record) throws IOException;

}