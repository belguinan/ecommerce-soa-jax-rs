package com.example.userservice.config;

import com.example.userservice.auth.AuthContext;
import com.example.userservice.auth.JerseyRequestContextFilter;
import com.example.userservice.errors.GlobalExceptionMapper;
import com.example.userservice.errors.ValidationExceptionMapper;
import com.example.userservice.auth.AuthenticationFilter;
import com.example.userservice.resources.UserResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.register(UserResource.class);
        this.register(AuthenticationFilter.class);
        this.register(ValidationExceptionMapper.class);
        this.register(GlobalExceptionMapper.class);
        this.register(AuthContext.class);
        this.register(JerseyRequestContextFilter.class);
    }
}
