package com.example.userservice.auth;

import io.jsonwebtoken.Claims;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

@Provider
@Component
public class AuthContext {

    private final JwtService jwtService;

    public AuthContext(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public Long getCurrentUserId() {
        var requestContext = JerseyRequestContextFilter.getCurrentContext();
        if (requestContext == null) {
            throw new IllegalStateException("Request context not available");
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No valid authorization header found");
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        Claims claims = this.jwtService.validateToken(token);

        return Long.valueOf(claims.get("userId").toString());
    }
}