package org.taskflow.service;

import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.taskflow.dto.LoginRequest;
import org.taskflow.dto.LoginResponse;
import org.taskflow.dto.RegisterRequest;
import org.taskflow.dto.UserResponse;
import org.taskflow.model.User;
import org.taskflow.repository.UserRepository;

import java.time.LocalDateTime;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public LoginResponse createUser(RegisterRequest userRequest) {
        if (userRequest.getDisplayName() == null || userRequest.getDisplayName().isEmpty()) {
            throw new IllegalArgumentException("Display name is required");
        }
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User newUser = new User();
        newUser.setDisplayName(userRequest.getDisplayName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(hashPassword(userRequest.getPassword()));
        newUser.setRole("USER");
        newUser.setNotifyOnDue(userRequest.isNotifyOnDue());
        newUser.setCreatedAt(LocalDateTime.now());

        userRepository.registerUser(newUser);

        UserResponse userResponse = new UserResponse(
                userRequest.getEmail(),
                userRequest.getDisplayName(),
                "USER",
                userRequest.isNotifyOnDue()
        );

        String accessToken = JwtService.generateAccessToken(userResponse);
        String refreshToken = JwtService.generateRefreshToken(userResponse);

        return new LoginResponse("User registered successfully", accessToken, refreshToken, userResponse);
    }


    public LoginResponse login(LoginRequest userRequest) {
        if (userRequest.getEmail() == null || userRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRequest.getPassword() == null || userRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User existingUser = userRepository.findByEmail(userRequest.getEmail());

        if (existingUser == null) {
            throw new UnauthorizedException("Invalid Credentials");
        }

        if (!checkPassword(userRequest.getPassword(), existingUser.getPassword())) {
            throw new UnauthorizedException("Invalid Credentials");
        }

        UserResponse userResponse = new UserResponse(
                existingUser.getEmail(),
                existingUser.getDisplayName(),
                existingUser.getRole(),
                existingUser.isNotifyOnDue()
        );

        String accessToken = JwtService.generateAccessToken(userResponse);
        String refreshToken = JwtService.generateRefreshToken(userResponse);

        return new LoginResponse("Login successful", accessToken, refreshToken, userResponse);
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

