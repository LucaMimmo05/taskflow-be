package org.taskflow.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.taskflow.model.Collaborator;
import org.taskflow.model.Label;
import org.taskflow.model.Phase;

import java.util.List;

public class ProjectRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private List<@Valid Collaborator> collaborators;

    @NotNull(message = "Phases list cannot be null")
    @Size(min = 1, max = 3, message = "There must be at least one phase and at most 3 phases")
    private List<@Valid Phase> phases;

    private List<@Valid Label> labels;

    public ProjectRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<Collaborator> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators) {
        this.collaborators = collaborators;
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
