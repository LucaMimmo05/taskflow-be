package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.MessageResponse;
import org.taskflow.dto.SettingRequest;
import org.taskflow.dto.UserRequest;
import org.taskflow.dto.UserResponse;
import org.taskflow.service.UserService;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class UserController {

    @Inject
    UserService userService;

    @Inject
    Instance<JsonWebToken> jwtInstance;



    public ObjectId getCurrentUserId() {
        JsonWebToken jwt = jwtInstance.get();
        return new ObjectId(jwt.getSubject());
    }

    @PUT
    public UserResponse updateUser(UserRequest userRequest) {
        return userService.updateUser(getCurrentUserId(), userRequest);
    }

    @Path("/settings")
    @PUT
    public MessageResponse updateUserSettings(SettingRequest userRequest) {
        return userService.updateNotifySetting(getCurrentUserId(), userRequest.isNotifyOnDue());
    }

    @DELETE
    public MessageResponse deleteUser() {
        ObjectId userId = getCurrentUserId();
        userService.deleteUser(userId);
        return new MessageResponse("User has been deleted");
    }
    
}

