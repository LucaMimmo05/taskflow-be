package org.taskflow.controller;

import com.mongodb.client.MongoClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.bson.Document;

@Path("/health")
public class HealthController {

    @Inject
    MongoClient mongoClient;

    @GET
    public Response checkConnection() {
        try {
            mongoClient.getDatabase("admin").runCommand(new Document("ping", 1));
            return Response.ok("✅ MongoDB connection OK!").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("❌ MongoDB connection FAILED: " + e.getMessage()).build();
        }
    }
}
