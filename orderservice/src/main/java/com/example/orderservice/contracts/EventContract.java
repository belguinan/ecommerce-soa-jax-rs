package com.example.orderservice.contracts;

public interface EventContract<T> {

    public String getEventId();

    public String getEventTopic();

    static <T, E extends EventContract<T>> E from(T entity) {
        throw new UnsupportedOperationException("Must be implemented by concrete class");
    }
}