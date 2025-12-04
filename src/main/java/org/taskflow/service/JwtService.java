package org.taskflow.service;

import io.smallrye.jwt.build.Jwt;
import org.taskflow.dto.UserResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public class JwtService {

    public static String generateAccessToken(UserResponse user) {
        return Jwt.issuer("taskflow-app")
                .subject(user.getId())
                .upn(user.getEmail())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("name", user.getDisplayName())
                .groups(Set.of("access-token", "user"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofMinutes(15)))
                .sign();
    }

    public static String generateRefreshToken(UserResponse user) {
        return Jwt.issuer("taskflow-app")
                .subject(user.getId())
                .upn(user.getEmail())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("name", user.getDisplayName())
                .groups(Set.of("refresh-token"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofDays(7)))
                .sign();
    }
}
