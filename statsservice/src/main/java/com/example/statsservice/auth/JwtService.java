package com.example.statsservice.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {
    private static final long EXPIRATION_TIME = 60 * 60 * 24 * 7;
    private static final String TOKEN_PREFIX = "jwt_token:";
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
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
        claims.put("userId", userInfo.toString());

        return claims;
    }
}