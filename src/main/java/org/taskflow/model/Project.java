package org.taskflow.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@MongoEntity(collection = "projects")
public class Project {

    private ObjectId id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "CreatorId is required")
    private ObjectId creatorId;

    private List<Collaborator> collaborators;

    @NotNull(message = "Phases list cannot be null")
    @Size(min = 1, message = "There must be at least one phase")
    private List<Phase> phases;

    private List<Label> labels;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Project() {
    }

    public Project(ObjectId id, String title, ObjectId creatorId, List<Collaborator> collaborators, List<Phase> phases, List<Label> labels, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.creatorId = creatorId;
        this.collaborators = collaborators;
        this.phases = phases;
        this.labels = labels;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ObjectId getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(ObjectId creatorId) {
        this.creatorId = creatorId;
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
