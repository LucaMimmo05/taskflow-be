package org.taskflow.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Phase {
    @NotBlank(message = "Phase id cannot be empty")
    private String id;

    @NotBlank(message = "Phase title cannot be empty")
    private String title;

    @Min(value = 0, message = "Position cannot be negative")
    private int position;

    public Phase(String id, String title, int position) {
        this.id = id;
        this.title = title;
        this.position = position;
    }
    public Phase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}