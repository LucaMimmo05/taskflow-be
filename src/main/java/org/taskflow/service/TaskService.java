package org.taskflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.taskflow.dto.AssigneeResponse;
import org.taskflow.dto.TaskRequest;
import org.taskflow.dto.TaskResponse;
import org.taskflow.exception.BadRequestException;
import org.taskflow.model.*;
import org.taskflow.repository.ProjectRepository;
import org.taskflow.repository.TaskRepository;
import org.taskflow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;


    public List<TaskResponse> getTasksByProjectId(ObjectId projectId) {
        var tasks = taskRepository.getTasksByProjectId(projectId);
        return tasks.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse createTask(TaskRequest taskRequest, ObjectId projectId, ObjectId userId) {
        Project project = projectRepository.findById(projectId);
        if (project == null) {
            throw new BadRequestException("Project not found");
        }

        boolean phaseExists = project.getPhases().stream()
                .anyMatch(phase -> phase.getId().equals(taskRequest.getPhaseId()));

        if (!phaseExists) {
            throw new BadRequestException("Phase ID does not exist in this project");
        }

        if (taskRequest.getLabelIds() != null && !taskRequest.getLabelIds().isEmpty()) {
            if (project.getLabels() == null || project.getLabels().isEmpty()) {
                throw new BadRequestException("Project does not have any labels");
            }

            List<String> projectLabelIds = project.getLabels().stream()
                    .map(Label::getId)
                    .toList();

            for (String labelId : taskRequest.getLabelIds()) {
                if (!projectLabelIds.contains(labelId)) {
                    throw new BadRequestException("Label ID '" + labelId + "' does not exist in this project");
                }
            }
        }

        List<ObjectId> assigneeIds = null;
        if (taskRequest.getAssignees() != null && !taskRequest.getAssignees().isEmpty()) {
            List<ObjectId> collaboratorIds = project.getCollaborators().stream()
                    .map(Collaborator::getUserId)
                    .toList();

            assigneeIds = taskRequest.getAssignees().stream()
                    .map(ObjectId::new)
                    .peek(assigneeId -> {
                        if (!collaboratorIds.contains(assigneeId)) {
                            throw new BadRequestException("User ID '" + assigneeId + "' is not a collaborator of this project");
                        }
                    })
                    .collect(Collectors.toList());
        }

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setProjectId(projectId);
        task.setPhaseId(taskRequest.getPhaseId());
        task.setLabelIds(taskRequest.getLabelIds());
        task.setAssignees(assigneeIds);
        task.setCreatedBy(userId);
        task.setDueDate(taskRequest.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        taskRepository.createTask(task);
        return toResponse(task);
    }


    public TaskResponse toResponse(Task task) {
        Project project = projectRepository.findById(task.getProjectId());

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId().toString());
        taskResponse.setProjectId(task.getProjectId().toString());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setPhaseId(task.getPhaseId());

        List<Label> labels = null;
        if (task.getLabelIds() != null && !task.getLabelIds().isEmpty() && project.getLabels() != null) {
            labels = task.getLabelIds().stream()
                    .map(labelId -> project.getLabels().stream()
                            .filter(label -> label.getId().equals(labelId))
                            .findFirst()
                            .orElse(null))
                    .filter(label -> label != null)
                    .collect(Collectors.toList());
        }
        taskResponse.setLabels(labels);

        List<AssigneeResponse> assigneeResponses = task.getAssignees().stream()
                .map(assigneeId -> {
                    AssigneeResponse assignee = new AssigneeResponse();
                    assignee.setUserId(assigneeId.toString());

                    User user = userRepository.findById(assigneeId);
                    assignee.setDisplayName(user != null ? user.getDisplayName() : "Unknown");

                    return assignee;
                })
                .collect(Collectors.toList());

        taskResponse.setAssignees(assigneeResponses);
        taskResponse.setCreatedBy(task.getCreatedBy().toString());

        User creator = userRepository.findById(task.getCreatedBy());
        taskResponse.setCreatedByName(creator != null ? creator.getDisplayName() : "Unknown");

        taskResponse.setCreatedAt(task.getCreatedAt());
        taskResponse.setUpdatedAt(task.getUpdatedAt());
        taskResponse.setDueDate(task.getDueDate());

        return taskResponse;
    }
}
