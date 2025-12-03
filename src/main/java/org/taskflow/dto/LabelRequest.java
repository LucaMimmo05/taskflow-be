package org.taskflow.dto;

import jakarta.validation.constraints.NotNull;

public class LabelRequest {
    @NotNull(message = "Label ID cannot be null")
    private String id;

    public LabelRequest(String id) {
        this.id = id;
    }
    public LabelRequest() {
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
