package org.taskflow.dto;

import jakarta.validation.Valid;
import org.taskflow.model.Label;
import org.taskflow.model.Phase;

import java.util.List;

public class ProjectUpdateRequest {

    private String title;

    private List<@Valid Phase> phases;

    private List<@Valid Label> labels;

    public ProjectUpdateRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }
}
