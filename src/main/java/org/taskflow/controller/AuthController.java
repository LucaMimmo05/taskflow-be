package org.taskflow.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.taskflow.dto.LoginRequest;
import org.taskflow.dto.LoginResponse;
import org.taskflow.dto.UserRequest;
import org.taskflow.service.UserService;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    @Inject
    UserService userService;

    @POST
    @Path("/register")
    public LoginResponse register(@Valid UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @POST
    @Path("/login")
    public LoginResponse login(@Valid LoginRequest userRequest) {
        return userService.login(userRequest);
    }
}
