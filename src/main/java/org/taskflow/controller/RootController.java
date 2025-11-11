package org.taskflow.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class RootController {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response index() {
        return Response.ok("Welcome to TaskFlow backend").build();
    }
}
