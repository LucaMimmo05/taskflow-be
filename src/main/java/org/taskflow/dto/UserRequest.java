package org.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "Display name cannot be empty")
    @Size(min = 3, max = 30, message = "Display name must be between 3 and 30 characters")
    private String displayName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;



    private boolean notifyOnDue = false;

    public UserRequest() {}

    public UserRequest(String displayName, String email, String password, boolean notifyOnDue) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.notifyOnDue = notifyOnDue;
    }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isNotifyOnDue() { return notifyOnDue; }
    public void setNotifyOnDue(boolean notifyOnDue) { this.notifyOnDue = notifyOnDue; }
}
