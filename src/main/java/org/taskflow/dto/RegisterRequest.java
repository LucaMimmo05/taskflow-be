package org.taskflow.dto;

public class UserRequest {
    private String email;
    private String displayName;
    private String password;
    private boolean notifyOnDue;
    public UserRequest() {
    }
    public UserRequest(String email, String displayName, String password, boolean notifyOnDue) {
        this.email = email;
        this.displayName = displayName;
        this.password = password;
        this.notifyOnDue = notifyOnDue;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNotifyOnDue() {
        return notifyOnDue;
    }

    public void setNotifyOnDue(boolean notifyOnDue) {
        this.notifyOnDue = notifyOnDue;
    }
}
