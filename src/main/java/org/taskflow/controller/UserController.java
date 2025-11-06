package org.taskflow.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.taskflow.dto.LoginRequest;
import org.taskflow.dto.LoginResponse;
import org.taskflow.dto.RegisterRequest;
import org.taskflow.service.UserService;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @POST
    public LoginResponse register(RegisterRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GET
    public LoginResponse login(LoginRequest userRequest) {
        return userService.login(userRequest);
    }
}

