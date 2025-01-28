package com.example.cartservice.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final String TOKEN_PREFIX = "jwt_token:";
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Claims validateToken(String token) {
        String redisKey = TOKEN_PREFIX + token;
        Object userInfo = redisTemplate.opsForValue().get(redisKey);

        if (userInfo == null) {
            throw new NotAuthorizedException("Invalid or expired token");
        }

        Claims claims = new DefaultClaims();
        claims.put("userId", Long.valueOf(userInfo.toString()));

        return claims;
    }
}