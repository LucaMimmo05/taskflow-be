package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.TaskRequest;
import org.taskflow.dto.TaskResponse;
import org.taskflow.service.TaskService;

import java.util.List;
import java.util.Map;

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

    @PUT
    @Path("/{taskId}")
    public TaskResponse updateTask(TaskRequest taskRequest, @PathParam("taskId") String taskId) {
        return taskService.updateTask(new ObjectId(taskId), taskRequest, getCurrentUserId());
    }

    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") String taskId) {
        taskService.deleteTask(new ObjectId(taskId), getCurrentUserId());
        return Response.ok(Map.of("message", "Task eliminata con successo")).build();
    }
}
