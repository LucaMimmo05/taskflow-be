package org.taskflow.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@MongoEntity(collection = "tasks")
public class Task {
    private ObjectId id;

    @NotNull(message = "Project ID cannot be null")
    private ObjectId projectId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    @NotBlank(message = "Phase ID cannot be blank")
    private String phaseId;

    private List<String> labelIds;
    private List<ObjectId> assignees;

    @NotNull(message = "Created by cannot be null")
    private ObjectId createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dueDate;

    public Task() {
    }

    public Task(ObjectId id, ObjectId projectId, String title, String description, String phaseId, List<String> labelIds, List<ObjectId> assignees, ObjectId createdBy, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime dueDate) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.phaseId = phaseId;
        this.labelIds = labelIds;
        this.assignees = assignees;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dueDate = dueDate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProjectId() {
        return projectId;
    }

    public void setProjectId(ObjectId projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public List<String> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<String> labelIds) {
        this.labelIds = labelIds;
    }

    public List<ObjectId> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<ObjectId> assignees) {
        this.assignees = assignees;
    }

    public ObjectId getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ObjectId createdBy) {
        this.createdBy = createdBy;
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

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
