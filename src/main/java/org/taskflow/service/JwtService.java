package org.taskflow.service;

import io.smallrye.jwt.build.Jwt;
import org.taskflow.dto.UserResponse;

import java.time.Duration;
import java.time.Instant;

public class JwtService {

    public static String generateAccessToken(UserResponse user) {
        try {
            return Jwt.issuer("taskflow-app")
                    .subject(user.getEmail())
                    .upn(user.getEmail())
                    .claim("email", user.getEmail())
                    .claim("name", user.getDisplayName())
                    .expiresIn(Duration.ofMinutes(15))
                    .issuedAt(Instant.now())
                    .sign();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate access token: " + e.getMessage(), e);
        }
    }

    public static String generateRefreshToken(UserResponse user) {
        try {
            return Jwt.issuer("taskflow-app")
                    .subject(user.getEmail())
                    .upn(user.getEmail())
                    .claim("email", user.getEmail())
                    .claim("name", user.getDisplayName())
                    .expiresIn(Duration.ofDays(7))
                    .issuedAt(Instant.now())
                    .sign();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate refresh token: " + e.getMessage(), e);
        }
    }
}

