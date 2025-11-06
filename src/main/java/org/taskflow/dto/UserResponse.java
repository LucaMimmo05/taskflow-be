package org.taskflow.dto;

import org.bson.types.ObjectId;

public class UserResponse {
    private String email;
    private String displayName;
    private String role;
    private boolean notifyOnDue;

    public UserResponse( String email, String displayName, String role, boolean notifyOnDue) {
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.notifyOnDue = notifyOnDue;
    }
    public UserResponse(ObjectId id, String email, String displayName, String role) {
        this.email = email;
        this.displayName = displayName;
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

    public boolean isNotifyOnDue() {
        return notifyOnDue;
    }
}
