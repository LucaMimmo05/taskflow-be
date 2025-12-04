package org.taskflow.service;

import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.taskflow.dto.UserResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public class JwtService {

    public static String generateAccessToken(UserResponse user) {
        try {
            JwtClaimsBuilder claims = Jwt.claims();
            claims.issuer("taskflow-app");
            claims.subject(user.getId());
            claims.upn(user.getEmail());
            claims.claim("userId", user.getId());
            claims.claim("email", user.getEmail());
            claims.claim("name", user.getDisplayName());
            claims.groups(Set.of("access-token", "user"));
            claims.issuedAt(Instant.now());
            claims.expiresAt(Instant.now().plus(Duration.ofMinutes(15)));

            return claims.sign("none");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate access token: " + e.getMessage(), e);
        }
    }

    public static String generateRefreshToken(UserResponse user) {
        try {
            JwtClaimsBuilder claims = Jwt.claims();
            claims.issuer("taskflow-app");
            claims.subject(user.getId());
            claims.upn(user.getEmail());
            claims.claim("userId", user.getId());
            claims.claim("email", user.getEmail());
            claims.claim("name", user.getDisplayName());
            claims.groups(Set.of("refresh-token"));
            claims.issuedAt(Instant.now());
            claims.expiresAt(Instant.now().plus(Duration.ofDays(7)));

            return claims.sign("none");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate refresh token: " + e.getMessage(), e);
        }
    }
}
