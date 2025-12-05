package org.taskflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.taskflow.dto.MessageResponse;
import org.taskflow.dto.NotificationCountResponse;
import org.taskflow.dto.NotificationResponse;
import org.taskflow.exception.BadRequestException;
import org.taskflow.exception.NotFoundException;
import org.taskflow.exception.UnauthorizedException;
import org.taskflow.model.Notification;
import org.taskflow.model.User;
import org.taskflow.repository.NotificationRepository;
import org.taskflow.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class NotificationService {

    @Inject
    NotificationRepository notificationRepository;

    @Inject
    UserRepository userRepository;

    /**
     * Crea una nuova notifica
     */
    public void createNotification(ObjectId recipientId, ObjectId senderId, String type,
                                   String message, ObjectId entityId, String entityType) {
        System.out.println("=== NotificationService.createNotification CHIAMATO ===");
        System.out.println("recipientId: " + recipientId);
        System.out.println("senderId: " + senderId);
        System.out.println("type: " + type);
        System.out.println("message: " + message);

        if (recipientId == null) {
            throw new BadRequestException("Recipient ID cannot be null");
        }
        if (senderId == null) {
            throw new BadRequestException("Sender ID cannot be null");
        }
        if (type == null || type.isBlank()) {
            throw new BadRequestException("Type cannot be blank");
        }
        if (message == null || message.isBlank()) {
            throw new BadRequestException("Message cannot be blank");
        }

        // Non inviare notifica se il sender è uguale al recipient
        if (recipientId.equals(senderId)) {
            System.out.println("Sender uguale a recipient - SKIP");
            return;
        }

        Notification notification = new Notification(
            recipientId, senderId, type, message, entityId, entityType
        );

        System.out.println("Chiamata al repository...");
        notificationRepository.createNotification(notification);
        System.out.println("=== NotificationService.createNotification COMPLETATO ===");
    }

    /**
     * Recupera tutte le notifiche non lette + le ultime 10 lette per l'utente loggato
     */
    public List<NotificationResponse> getNotifications(ObjectId userId) {
        System.out.println("=== NotificationService.getNotifications CHIAMATO ===");
        System.out.println("userId: " + userId);

        List<Notification> unreadNotifications = notificationRepository.findUnreadByRecipientId(userId);
        System.out.println("Unread notifications: " + unreadNotifications.size());

        List<Notification> readNotifications = notificationRepository.findReadByRecipientId(userId, 10);
        System.out.println("Read notifications: " + readNotifications.size());

        List<Notification> allNotifications = new ArrayList<>();
        allNotifications.addAll(unreadNotifications);
        allNotifications.addAll(readNotifications);

        System.out.println("Total notifications: " + allNotifications.size());
        System.out.println("=== NotificationService.getNotifications COMPLETATO ===");

        return allNotifications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Recupera il conteggio delle notifiche non lette per l'utente loggato
     */
    public NotificationCountResponse getUnreadCount(ObjectId userId) {
        long count = notificationRepository.countUnreadByRecipientId(userId);
        return new NotificationCountResponse(count);
    }

    /**
     * Marca una singola notifica come letta
     */
    public NotificationResponse markAsRead(ObjectId notificationId, ObjectId userId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId);

        if (notification == null) {
            throw new NotFoundException("Notification not found");
        }

        // Verifica che la notifica appartenga all'utente loggato
        if (!notification.getRecipientId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to mark this notification as read");
        }

        if (!notification.getIsRead()) {
            notificationRepository.markAsRead(notification);
        }

        return toResponse(notification);
    }

    /**
     * Marca tutte le notifiche non lette dell'utente come lette
     */
    public MessageResponse markAllAsRead(ObjectId userId) {
        notificationRepository.markAllAsReadByRecipientId(userId);
        return new MessageResponse("All notifications marked as read");
    }

    /**
     * Elimina le notifiche associate a un'entità (quando viene cancellata)
     */
    public void deleteNotificationsByEntityId(ObjectId entityId) {
        notificationRepository.deleteByEntityId(entityId);
    }

    /**
     * Converte Notification in NotificationResponse
     */
    private NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId() != null ? notification.getId().toString() : null);
        response.setRecipientId(notification.getRecipientId() != null ? notification.getRecipientId().toString() : null);
        response.setSenderId(notification.getSenderId() != null ? notification.getSenderId().toString() : null);

        // Recupera il nome del sender
        if (notification.getSenderId() != null) {
            User sender = userRepository.findById(notification.getSenderId());
            response.setSenderName(sender != null ? sender.getDisplayName() : "Unknown User");
        } else {
            response.setSenderName("Unknown User");
        }

        response.setType(notification.getType());
        response.setMessage(notification.getMessage());
        response.setEntityId(notification.getEntityId() != null ? notification.getEntityId().toString() : null);
        response.setEntityType(notification.getEntityType());
        response.setIsRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());

        return response;
    }
}

