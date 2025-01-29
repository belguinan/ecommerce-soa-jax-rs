package com.example.statsservice.config;

import com.example.statsservice.auth.AuthContext;
import com.example.statsservice.auth.CorsFilter;
import com.example.statsservice.auth.JerseyRequestContextFilter;
import com.example.statsservice.errors.GlobalExceptionMapper;
import com.example.statsservice.errors.ValidationExceptionMapper;
import com.example.statsservice.auth.AuthenticationFilter;
import com.example.statsservice.resources.StatsResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.register(CorsFilter.class);
        this.register(StatsResource.class);
        this.register(AuthenticationFilter.class);
        this.register(ValidationExceptionMapper.class);
        this.register(GlobalExceptionMapper.class);
        this.register(AuthContext.class);
        this.register(JerseyRequestContextFilter.class);
    }
}