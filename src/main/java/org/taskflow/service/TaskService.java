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
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    NotificationService notificationService;


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

        User creator = userRepository.findById(userId);
        String creatorName = creator != null ? creator.getDisplayName() : "Someone";

        // Notifica tutti i collaboratori del progetto della creazione della task
        List<ObjectId> collaboratorIds = project.getCollaborators().stream()
                .map(Collaborator::getUserId)
                .filter(collaboratorId -> !collaboratorId.equals(userId)) // Escludi il creatore
                .toList();

        for (ObjectId collaboratorId : collaboratorIds) {
            notificationService.createNotification(
                collaboratorId,
                userId,
                "taskCreated",
                creatorName + " ha creato una nuova task nel progetto: " + task.getTitle(),
                task.getId(),
                "task"
            );
        }

        // Crea notifiche aggiuntive per gli assignees
        if (assigneeIds != null && !assigneeIds.isEmpty()) {
            for (ObjectId assigneeId : assigneeIds) {
                notificationService.createNotification(
                    assigneeId,
                    userId,
                    "taskAssigned",
                    creatorName + " ti ha assegnato il task: " + task.getTitle(),
                    task.getId(),
                    "task"
                );
            }
        }

        return toResponse(task);
    }

    public TaskResponse updateTask(ObjectId taskId, TaskRequest taskRequest, ObjectId userId) {
        Task task = taskRepository.findByTaskId(taskId);
        if (task == null) {
            throw new BadRequestException("Task not found");
        }

        Project project = projectRepository.findById(task.getProjectId());
        if (project == null) {
            throw new BadRequestException("Project not found");
        }

        boolean isCollaborator = project.getCollaborators().stream()
                .anyMatch(c -> c.getUserId().equals(userId));
        if (!isCollaborator) {
            throw new BadRequestException("User is not a collaborator of this project");
        }

        if (taskRequest.getPhaseId() != null) {
            boolean phaseExists = project.getPhases().stream()
                    .anyMatch(phase -> phase.getId().equals(taskRequest.getPhaseId()));
            if (!phaseExists) {
                throw new BadRequestException("Phase ID does not exist in this project");
            }
            task.setPhaseId(taskRequest.getPhaseId());
        }

        if (taskRequest.getLabelIds() != null) {
            if (!taskRequest.getLabelIds().isEmpty() && (project.getLabels() == null || project.getLabels().isEmpty())) {
                throw new BadRequestException("Project does not have any labels");
            }

            if (!taskRequest.getLabelIds().isEmpty()) {
                List<String> projectLabelIds = project.getLabels().stream()
                        .map(Label::getId)
                        .toList();

                for (String labelId : taskRequest.getLabelIds()) {
                    if (!projectLabelIds.contains(labelId)) {
                        throw new BadRequestException("Label ID '" + labelId + "' does not exist in this project");
                    }
                }
            }
            task.setLabelIds(taskRequest.getLabelIds());
        }

        if (taskRequest.getAssignees() != null) {
            List<ObjectId> oldAssignees = task.getAssignees() != null ? task.getAssignees() : List.of();

            if (!taskRequest.getAssignees().isEmpty()) {
                List<ObjectId> collaboratorIds = project.getCollaborators().stream()
                        .map(Collaborator::getUserId)
                        .toList();

                List<ObjectId> assigneeIds = taskRequest.getAssignees().stream()
                        .map(ObjectId::new)
                        .peek(assigneeId -> {
                            if (!collaboratorIds.contains(assigneeId)) {
                                throw new BadRequestException("User ID '" + assigneeId + "' is not a collaborator of this project");
                            }
                        })
                        .collect(Collectors.toList());
                task.setAssignees(assigneeIds);

                // Notifica i nuovi assignees
                User updater = userRepository.findById(userId);
                String updaterName = updater != null ? updater.getDisplayName() : "Someone";

                for (ObjectId assigneeId : assigneeIds) {
                    if (!oldAssignees.contains(assigneeId)) {
                        notificationService.createNotification(
                            assigneeId,
                            userId,
                            "taskAssigned",
                            updaterName + " ti ha assegnato il task: " + task.getTitle(),
                            task.getId(),
                            "task"
                        );
                    }
                }
            } else {
                task.setAssignees(List.of());
            }
        }

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
        task.setDueDate(taskRequest.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());

        taskRepository.updateTask(task);
        return toResponse(task);
    }

    public void deleteTask(ObjectId taskId, ObjectId userId) {
        Task task = taskRepository.findByTaskId(taskId);
        if (task == null) {
            throw new BadRequestException("Task not found");
        }

        Project project = projectRepository.findById(task.getProjectId());
        if (project == null) {
            throw new BadRequestException("Project not found");
        }

        boolean isCollaborator = project.getCollaborators().stream()
                .anyMatch(c -> c.getUserId().equals(userId));
        if (!isCollaborator) {
            throw new BadRequestException("User is not a collaborator of this project");
        }

        // Elimina le notifiche associate al task
        notificationService.deleteNotificationsByEntityId(taskId);

        taskRepository.deleteTask(taskId);
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
        if (project != null && task.getLabelIds() != null && !task.getLabelIds().isEmpty() && project.getLabels() != null) {
            labels = task.getLabelIds().stream()
                    .map(labelId -> project.getLabels().stream()
                            .filter(label -> label.getId().equals(labelId))
                            .findFirst()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        taskResponse.setLabels(labels);

        List<AssigneeResponse> assigneeResponses = new java.util.ArrayList<>();
        if (task.getAssignees() != null && !task.getAssignees().isEmpty()) {
            assigneeResponses = task.getAssignees().stream()
                    .map(assigneeId -> {
                        AssigneeResponse assignee = new AssigneeResponse();
                        assignee.setUserId(assigneeId.toString());

                        User user = userRepository.findById(assigneeId);
                        assignee.setDisplayName(user != null ? user.getDisplayName() : "Unknown");

                        return assignee;
                    })
                    .collect(Collectors.toList());
        }

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
