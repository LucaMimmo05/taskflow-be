package org.taskflow.controller;

import io.quarkus.security.Authenticated;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.taskflow.dto.MessageResponse;
import org.taskflow.dto.NotificationCountResponse;
import org.taskflow.dto.NotificationResponse;
import org.taskflow.service.NotificationService;

import java.util.List;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class NotificationController {

    @Inject
    NotificationService notificationService;

    @Inject
    Instance<JsonWebToken> jwtInstance;

    private ObjectId getCurrentUserId() {
        JsonWebToken jwt = jwtInstance.get();
        return new ObjectId(jwt.getSubject());
    }

    /**
     * GET /api/notifications
     * Recupera tutte le notifiche non lette + ultime 10 lette per l'utente loggato
     */
    @GET
    public List<NotificationResponse> getNotifications() {
        return notificationService.getNotifications(getCurrentUserId());
    }

    /**
     * GET /api/notifications/count
     * Restituisce il conteggio delle notifiche non lette per l'utente loggato
     */
    @GET
    @Path("/count")
    public NotificationCountResponse getUnreadCount() {
        return notificationService.getUnreadCount(getCurrentUserId());
    }

    /**
     * PUT /api/notifications/{id}/read
     * Marca una singola notifica come letta
     */
    @PUT
    @Path("/{id}/read")
    public NotificationResponse markAsRead(@PathParam("id") String notificationId) {
        return notificationService.markAsRead(new ObjectId(notificationId), getCurrentUserId());
    }

    /**
     * PUT /api/notifications/read-all
     * Marca tutte le notifiche non lette dell'utente come lette
     */
    @PUT
    @Path("/read-all")
    public Response markAllAsRead() {
        MessageResponse message = notificationService.markAllAsRead(getCurrentUserId());
        return Response.ok(message).build();
    }
}

