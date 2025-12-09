package org.taskflow.service;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.taskflow.model.Task;
import org.taskflow.model.User;
import org.taskflow.repository.TaskRepository;
import org.taskflow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class TaskSchedulerService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    NotificationService notificationService;

    @Scheduled(every = "1h")
    void checkDueTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24Hours = now.plusHours(24);

        List<Task> tasks = taskRepository.findTasksDueSoon(now, in24Hours);

        for (Task task : tasks) {
            if (task.getAssignees() != null && !task.getAssignees().isEmpty()) {
                for (ObjectId assigneeId : task.getAssignees()) {
                    User assignee = userRepository.findById(assigneeId);
                    if (assignee != null) {
                        String message = "La task \"" + task.getTitle() + "\" scadrà tra meno di 24 ore!";

                        notificationService.createNotification(
                            assigneeId,
                            task.getCreatedBy(),
                            "taskDueSoon",
                            message,
                            task.getId(),
                            "task"
                        );
                    }
                }
            }
        }
    }
}

