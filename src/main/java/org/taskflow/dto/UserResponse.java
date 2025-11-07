package org.taskflow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserResponse {
    private String id;
    private String email;
    private String displayName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String role;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean notifyOnDue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public UserResponse(String id, String email, String displayName, String role, boolean notifyOnDue) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.role = role;
        this.notifyOnDue = notifyOnDue;
    }

    public UserResponse(String id, String email, String displayName) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
    }

    public UserResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
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
