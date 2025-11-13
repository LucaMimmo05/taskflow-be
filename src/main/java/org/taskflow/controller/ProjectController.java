package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.ProjectRequest;
import org.taskflow.dto.ProjectResponse;
import org.taskflow.service.ProjectService;

import java.util.List;

@Path("/api/project")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class ProjectController {

    @Inject
    ProjectService projectService;

    @Inject
    Instance<JsonWebToken> jwtInstance;



    public ObjectId getCurrentUserId() {
        JsonWebToken jwt = jwtInstance.get();
        return new ObjectId(jwt.getSubject());
    }

    @GET
    public List<ProjectResponse> getProjectsByUserId() {
        ObjectId currentUserId = getCurrentUserId();
        return projectService.getByUserId(currentUserId);
    }

    @POST
    public ProjectResponse createProject(@Valid ProjectRequest projectRequest) {
        ObjectId currentUserId = getCurrentUserId();
        return projectService.create(projectRequest, currentUserId);
    }
}
