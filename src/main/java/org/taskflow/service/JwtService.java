package org.taskflow.service;

import org.taskflow.dto.UserResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

public class JwtService {

    public static String generateAccessToken(UserResponse user) {
        long now = Instant.now().getEpochSecond();
        long exp = Instant.now().plus(Duration.ofMinutes(15)).getEpochSecond();

        String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
                "{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes());

        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                String.format("{\"iss\":\"taskflow-app\",\"sub\":\"%s\",\"upn\":\"%s\",\"userId\":\"%s\",\"email\":\"%s\",\"name\":\"%s\",\"groups\":[\"access-token\",\"user\"],\"iat\":%d,\"exp\":%d}",
                        user.getId(), user.getEmail(), user.getId(), user.getEmail(), user.getDisplayName(), now, exp).getBytes());

        return header + "." + payload + ".";
    }

    public static String generateRefreshToken(UserResponse user) {
        long now = Instant.now().getEpochSecond();
        long exp = Instant.now().plus(Duration.ofDays(7)).getEpochSecond();

        String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
                "{\"alg\":\"none\",\"typ\":\"JWT\"}".getBytes());

        String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                String.format("{\"iss\":\"taskflow-app\",\"sub\":\"%s\",\"upn\":\"%s\",\"userId\":\"%s\",\"email\":\"%s\",\"name\":\"%s\",\"groups\":[\"refresh-token\"],\"iat\":%d,\"exp\":%d}",
                        user.getId(), user.getEmail(), user.getId(), user.getEmail(), user.getDisplayName(), now, exp).getBytes());

        return header + "." + payload + ".";
    }
}
