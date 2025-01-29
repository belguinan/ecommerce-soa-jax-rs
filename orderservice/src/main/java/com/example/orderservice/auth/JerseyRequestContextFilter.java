package com.example.orderservice.auth;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Provider
@Component
@Priority(Priorities.USER)
public class JerseyRequestContextFilter implements ContainerRequestFilter {

    private static final ThreadLocal<ContainerRequestContext> requestContext = new ThreadLocal<>();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        JerseyRequestContextFilter.requestContext.set(requestContext);
    }

    public static ContainerRequestContext getCurrentContext() {
        return requestContext.get();
    }

    public static void clearContext() {
        requestContext.remove();
    }
}