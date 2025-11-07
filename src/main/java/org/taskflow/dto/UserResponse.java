package org.taskflow.dto;

public class UserResponse {
    private String id;
    private String email;
    private String displayName;
    private String role;
    private boolean notifyOnDue;

    public UserResponse(String id, String email, String displayName, String role, boolean notifyOnDue) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.notifyOnDue = notifyOnDue;
    }

    public String getId() {
        return id;
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
