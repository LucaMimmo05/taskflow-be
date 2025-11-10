package org.taskflow.dto;

import org.taskflow.model.Label;
import org.taskflow.model.Phase;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectResponse {

    private String id;
    private String title;
    private String creatorName;
    private List<CollaboratorResponse> collaborators;
    private List<Phase> phases;
    private List<Label> labels;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProjectResponse() {
    }

    public ProjectResponse(String creatorName, String id, String title, List<CollaboratorResponse> collaborators, List<Phase> phases, List<Label> labels, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.creatorName = creatorName;
        this.id = id;
        this.title = title;
        this.collaborators = collaborators;
        this.phases = phases;
        this.labels = labels;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<CollaboratorResponse> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<CollaboratorResponse> collaborators) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
