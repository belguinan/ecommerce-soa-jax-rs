package com.example.productservice.config;

import com.example.productservice.auth.AuthContext;
import com.example.productservice.auth.JerseyRequestContextFilter;
import com.example.productservice.errors.GlobalExceptionMapper;
import com.example.productservice.errors.ValidationExceptionMapper;
import com.example.productservice.auth.AuthenticationFilter;
import com.example.productservice.resources.ProductResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.register(ProductResource.class);
        this.register(AuthenticationFilter.class);
        this.register(ValidationExceptionMapper.class);
        this.register(GlobalExceptionMapper.class);
        this.register(AuthContext.class);
        this.register(JerseyRequestContextFilter.class);
    }
}
