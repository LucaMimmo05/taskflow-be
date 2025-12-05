package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.taskflow.model.Notification;

import java.util.List;

@ApplicationScoped
public class NotificationRepository implements PanacheMongoRepository<Notification> {

    public void createNotification(Notification notification) {
        System.out.println("=== NotificationRepository.createNotification ===");
        System.out.println("Notification before persist: " + notification);
        System.out.println("ID before persist: " + notification.getId());

        // Usa persist diretto
        persist(notification);

        System.out.println("ID after persist: " + notification.getId());
        System.out.println("Notification persisted successfully");
    }

    public Notification findByNotificationId(ObjectId notificationId) {
        return find("_id", notificationId).firstResult();
    }

    public List<Notification> findUnreadByRecipientId(ObjectId recipientId) {
        System.out.println("=== findUnreadByRecipientId for recipientId: " + recipientId);
        List<Notification> result = list("recipientId = ?1 and isRead = ?2", Sort.descending("createdAt"), recipientId, false);
        System.out.println("Found " + result.size() + " unread notifications");
        return result;
    }

    public List<Notification> findReadByRecipientId(ObjectId recipientId, int limit) {
        return find("recipientId = ?1 and isRead = ?2", Sort.descending("createdAt"), recipientId, true)
                .page(0, limit)
                .list();
    }

    public long countUnreadByRecipientId(ObjectId recipientId) {
        return count("recipientId = ?1 and isRead = ?2", recipientId, false);
    }

    public void markAsRead(Notification notification) {
        notification.setIsRead(true);
        persistOrUpdate(notification);
    }

    public void markAllAsReadByRecipientId(ObjectId recipientId) {
        List<Notification> unreadNotifications = list("recipientId = ?1 and isRead = ?2", recipientId, false);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            persistOrUpdate(notification);
        }
    }

    public void deleteByEntityId(ObjectId entityId) {
        delete("entityId", entityId);
    }
}

