package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.UserRequest;
import org.taskflow.service.UserService;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;

    @Inject
    Instance<JsonWebToken> jwtInstance;



    public ObjectId getCurrentUserId() {
        JsonWebToken jwt = jwtInstance.get();
        return new ObjectId(jwt.getSubject());
    }

    @Path("/settings")
    @PUT
    @Authenticated
    public Response updateUserSettings(UserRequest userRequest) {
        userService.updateNotifySetting(getCurrentUserId(), userRequest.isNotifyOnDue());
        return Response.ok().entity("User settings updated successfully").build();
    }
    
}

