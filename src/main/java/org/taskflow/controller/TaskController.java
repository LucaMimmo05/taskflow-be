package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.TaskRequest;
import org.taskflow.dto.TaskResponse;
import org.taskflow.service.TaskService;

import java.util.List;

@Path("/api/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class TaskController {

    @Inject
    TaskService taskService;

    @Inject
    Instance<JsonWebToken> jwtInstance;

    public ObjectId getCurrentUserId() {
        JsonWebToken jwt = jwtInstance.get();
        return new ObjectId(jwt.getSubject());
    }

    @GET
    @Path("/{projectId}")
    public List<TaskResponse> getTasksByProjectId(@PathParam("projectId") String projectId) {
        return taskService.getTasksByProjectId(new ObjectId(projectId));
    }

    @POST
    @Path("/{projectId}")
    public TaskResponse createTask(TaskRequest taskRequest, @PathParam("projectId") String projectId) {
        return taskService.createTask(taskRequest, new ObjectId(projectId), getCurrentUserId());
    }
}
