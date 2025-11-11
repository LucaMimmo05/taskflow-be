package org.taskflow.controller;

import jakarta.ws.rs.Path;

@Path("/")
public class AzureController {

    @Path("/hello")
    public String hello() {
        return "Hello, Azure!";
    }
}
