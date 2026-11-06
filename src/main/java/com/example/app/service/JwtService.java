package com.example.app.service;

import com.example.app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60; // 1 hour

    private final SecretKey key;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must be configured with at least 32 UTF-8 bytes"
            );
        }
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Generate JWT
    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getId())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )
                .signWith(key)
                .compact();
    }

    // Extract user ID from JWT
    public String extractUserId(String token) {

        return getClaims(token).getSubject();
    }

    // Validate JWT
    public boolean isTokenValid(String token) {

        try {
            getClaims(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // Parse JWT
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}