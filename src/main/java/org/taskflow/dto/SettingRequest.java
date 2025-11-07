package org.taskflow.dto;

public class SettingRequest {
    boolean notifyOnDue;
    public SettingRequest() {
    }
    public SettingRequest(boolean notifyOnDue) {
        this.notifyOnDue = notifyOnDue;
    }
    public boolean isNotifyOnDue() {
        return notifyOnDue;
    }
    public void setNotifyOnDue(boolean notifyOnDue) {
        this.notifyOnDue = notifyOnDue;
    }
}
