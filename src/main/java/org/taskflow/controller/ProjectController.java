package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.*;
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

    @PUT
    @Path("/{id}")
    public ProjectResponse updateProject(@Valid ProjectUpdateRequest projectUpdateRequest, @PathParam("id") String id) {
        ObjectId currentUserId = getCurrentUserId();
        return projectService.update(projectUpdateRequest, currentUserId, new ObjectId(id));
    }

    @DELETE
    @Path("/{id}")
    public MessageResponse deleteProject(@PathParam("id") String id) {
        ObjectId currentUserId = getCurrentUserId();
        projectService.delete(currentUserId, new ObjectId(id));
        return new MessageResponse("Project deleted successfully");
    }

    @POST
    @Path("/{id}/collaborators")
    public ProjectResponse addCollaborator(@Valid CollaboratorRequest collaboratorRequest, @PathParam("id") String id) {
        ObjectId currentUserId = getCurrentUserId();
        return projectService.addCollaborator(new ObjectId(id), currentUserId, collaboratorRequest);
    }

    @DELETE
    @Path("/{id}/collaborators")
    public ProjectResponse removeCollaborator(@Valid CollaboratorRequest collaboratorRequest,@PathParam("id") String id, @PathParam("collaboratorId") String collaboratorId) {
        ObjectId currentUserId = getCurrentUserId();
        return projectService.removeCollaborator(new ObjectId(id), currentUserId, new ObjectId(collaboratorRequest.getUserId()));
    }
}
