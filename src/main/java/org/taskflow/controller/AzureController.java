package org.taskflow.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class AzureController {

    @Path("/hello")
    @GET
    public String hello() {
        return "Hello, Azure!";
    }
}
