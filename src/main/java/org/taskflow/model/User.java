package org.taskflow.model;


import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;
import java.time.LocalDateTime;

@MongoEntity(collection = "users")
public class User {
    private ObjectId id;
    private String displayName;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private boolean notifyOnDue;


    public User(ObjectId id, String displayName, String email, String password, LocalDateTime createdAt, boolean notifyOnDue) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.notifyOnDue = notifyOnDue;
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
