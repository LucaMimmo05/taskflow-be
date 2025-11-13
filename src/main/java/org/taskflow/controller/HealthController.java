package org.taskflow.controller;

import com.mongodb.client.MongoClient;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.bson.Document;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/health")
@Authenticated
public class HealthController {

    @Inject
    MongoClient mongoClient;

    @Inject
    JsonWebToken jwt;

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

    @GET
    @Path("/jwt")
    public Response check() {
        return Response.ok(jwt.getClaim("upn")).build();
    }
}
