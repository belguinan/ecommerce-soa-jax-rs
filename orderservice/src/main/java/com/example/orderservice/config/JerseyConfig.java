package com.example.orderservice.config;

import com.example.orderservice.auth.AuthContext;
import com.example.orderservice.auth.CorsFilter;
import com.example.orderservice.auth.JerseyRequestContextFilter;
import com.example.orderservice.errors.GlobalExceptionMapper;
import com.example.orderservice.errors.ValidationExceptionMapper;
import com.example.orderservice.auth.AuthenticationFilter;
import com.example.orderservice.producers.EventProducer;
import com.example.orderservice.resources.OrderResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.register(CorsFilter.class);
        this.register(OrderResource.class);
        this.register(AuthenticationFilter.class);
        this.register(ValidationExceptionMapper.class);
        this.register(GlobalExceptionMapper.class);
        this.register(AuthContext.class);
        this.register(JerseyRequestContextFilter.class);
        this.register(EventProducer.class);
    }
}