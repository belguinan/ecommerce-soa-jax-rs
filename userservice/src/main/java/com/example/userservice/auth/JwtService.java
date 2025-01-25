package com.example.userservice.auth;

import com.example.userservice.database.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    private static final long EXPIRATION_TIME = 60 * 60 * 24 * 7;
    private static final String TOKEN_PREFIX = "jwt_token:";
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * @param redisTemplate
     */
    public JwtService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param user
     * @return
     */
    public String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * 1000))
                .signWith(key)
                .compact();

        Map<String, Object> userInfo = new HashMap<>();

        userInfo.put("userId", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("phoneNumber", user.getPhoneNumber());
        userInfo.put("address", user.getAddress());
        userInfo.put("city", user.getCity());

        String redisKey = TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, userInfo, EXPIRATION_TIME, TimeUnit.SECONDS);

        return token;
    }

    public Claims validateToken(String token) {
        String redisKey = TOKEN_PREFIX + token;
        Object userInfo = redisTemplate.opsForValue().get(redisKey);

        if (userInfo == null) {
            throw new IllegalStateException("Token not found or expired");
        }

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void invalidateToken(String token) {
        String redisKey = TOKEN_PREFIX + token;
        redisTemplate.delete(redisKey);
    }
}