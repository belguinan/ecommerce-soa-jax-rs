package com.example.cartservice.config;

import com.example.cartservice.auth.AuthContext;
import com.example.cartservice.auth.CorsFilter;
import com.example.cartservice.auth.JerseyRequestContextFilter;
import com.example.cartservice.errors.GlobalExceptionMapper;
import com.example.cartservice.errors.ValidationExceptionMapper;
import com.example.cartservice.auth.AuthenticationFilter;
import com.example.cartservice.resources.CartResource;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        this.register(CorsFilter.class);
        this.register(CartResource.class);
        this.register(AuthenticationFilter.class);
        this.register(ValidationExceptionMapper.class);
        this.register(GlobalExceptionMapper.class);
        this.register(AuthContext.class);
        this.register(JerseyRequestContextFilter.class);
    }
}
