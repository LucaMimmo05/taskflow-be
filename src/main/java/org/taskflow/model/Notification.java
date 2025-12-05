package org.taskflow.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@MongoEntity(collection = "notifications")
public class Notification {
    private ObjectId id;

    @NotNull(message = "Recipient ID cannot be null")
    private ObjectId recipientId;

    @NotNull(message = "Sender ID cannot be null")
    private ObjectId senderId;

    @NotBlank(message = "Type cannot be blank")
    private String type;

    @NotBlank(message = "Message cannot be blank")
    private String message;

    private ObjectId entityId;

    private String entityType;

    @NotNull(message = "isRead cannot be null")
    private Boolean isRead;

    @NotNull(message = "Created at cannot be null")
    private LocalDateTime createdAt;

    public Notification() {
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public Notification(ObjectId recipientId, ObjectId senderId, String type, String message,
                       ObjectId entityId, String entityType) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.type = type;
        this.message = message;
        this.entityId = entityId;
        this.entityType = entityType;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(ObjectId recipientId) {
        this.recipientId = recipientId;
    }

    public ObjectId getSenderId() {
        return senderId;
    }

    public void setSenderId(ObjectId senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectId getEntityId() {
        return entityId;
    }

    public void setEntityId(ObjectId entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", recipientId=" + recipientId +
                ", senderId=" + senderId +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                ", entityId=" + entityId +
                ", entityType='" + entityType + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}

