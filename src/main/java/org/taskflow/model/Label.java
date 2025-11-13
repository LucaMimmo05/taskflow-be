package org.taskflow.model;

import jakarta.validation.constraints.NotBlank;

public class Label {
    @NotBlank(message = "Label id cannot be empty")
    private String id;

    @NotBlank(message = "Label title cannot be empty")
    private String title;

    @NotBlank(message = "Label color cannot be empty")
    private String color;

    public Label(String id, String title, String color) {
        this.id = id;
        this.title = title;
        this.color = color;
    }
    public Label() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
