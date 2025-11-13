package org.taskflow.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class Collaborator {
    @NotNull(message = "UserId is required")
    private ObjectId userId;

    @NotBlank(message = "Role cannot be empty")
    private String role;

    @NotNull(message = "JoinedAt cannot be null")
    private LocalDateTime joinedAt;



    public Collaborator(ObjectId userId, String role, LocalDateTime joinedAt) {
        this.userId = userId;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public Collaborator() {
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}
