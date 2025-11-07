package org.taskflow.model;


import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@MongoEntity(collection = "users")
public class User {
    private ObjectId id;
    @NotBlank(message = "Display name cannot be empty")
    @Size(min = 3, max = 30, message = "Display name must be between 3 and 30 characters")
    private String displayName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotNull(message = "Creation date cannot be null")
    private LocalDateTime createdAt;

    @NotBlank(message = "Role cannot be empty")
    private String role;

    private boolean notifyOnDue;


    public User(ObjectId id, String displayName, String email, String password,String role, LocalDateTime createdAt, boolean notifyOnDue) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.notifyOnDue = notifyOnDue;
    }

    public User() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isNotifyOnDue() {
        return notifyOnDue;
    }

    public void setNotifyOnDue(boolean notifyOnDue) {
        this.notifyOnDue = notifyOnDue;
    }
}
