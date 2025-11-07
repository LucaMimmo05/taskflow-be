package org.taskflow.service;

import io.smallrye.jwt.build.Jwt;
import org.taskflow.dto.UserResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class JwtService {

    public static String generateAccessToken(UserResponse user) {
        try {
            return Jwt.issuer("taskflow-app")
                    .subject(user.getId())
                    .upn(user.getEmail())
                    .claim("userId", user.getId())
                    .claim("email", user.getEmail())
                    .claim("name", user.getDisplayName())
                    .groups(new HashSet<>(Set.of("access-token", "user")))
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
                    .subject(user.getId())
                    .upn(user.getEmail())
                    .claim("userId", user.getId())
                    .claim("email", user.getEmail())
                    .claim("name", user.getDisplayName())
                    .groups(new HashSet<>(Set.of("refresh-token")))
                    .expiresIn(Duration.ofDays(7))
                    .issuedAt(Instant.now())
                    .sign();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate refresh token: " + e.getMessage(), e);
        }
    }
}
