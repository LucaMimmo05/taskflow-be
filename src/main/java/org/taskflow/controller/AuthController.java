package org.taskflow.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.taskflow.dto.LoginRequest;
import org.taskflow.dto.LoginResponse;
import org.taskflow.dto.UserRequest;
import org.taskflow.service.UserService;
@Path("/api/auth")
public class AuthController {
    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public LoginResponse register(UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @POST
    @Path("/login")
    public LoginResponse login(LoginRequest userRequest) {
        return userService.login(userRequest);
    }
}
