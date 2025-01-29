package com.example.cartservice.auth;

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
        Claims claims = this.getStoredClaims();
        Long userId = Long.valueOf(claims.get("userId").toString().split(":")[0]);
        return userId;
    }

    public boolean isSuperAdmin() {
        Claims claims = this.getStoredClaims();
        return String.valueOf(claims.get("userId").toString().split(":")[1]).equals("admin");
    }

    private Claims getStoredClaims()
    {
        var requestContext = JerseyRequestContextFilter.getCurrentContext();
        if (requestContext == null) {
            throw new IllegalStateException("Request context not available");
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("No valid authorization header found");
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        return this.jwtService.validateToken(token);
    }
}