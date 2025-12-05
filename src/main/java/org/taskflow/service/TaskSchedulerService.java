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

    /**
     * Controlla le task in scadenza ogni ora
     * Notifica gli assignees delle task che scadono nelle prossime 24 ore
     */
    @Scheduled(every = "1h")
    void checkDueTasks() {
        System.out.println("=== TaskSchedulerService: Controllo task in scadenza ===");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24Hours = now.plusHours(24);

        // Recupera tutte le task che scadono nelle prossime 24 ore
        List<Task> tasks = taskRepository.findTasksDueSoon(now, in24Hours);

        System.out.println("Task in scadenza trovate: " + tasks.size());

        for (Task task : tasks) {
            if (task.getAssignees() != null && !task.getAssignees().isEmpty()) {
                // Notifica tutti gli assignees
                for (ObjectId assigneeId : task.getAssignees()) {
                    User assignee = userRepository.findById(assigneeId);
                    if (assignee != null) {
                        String message = "La task \"" + task.getTitle() + "\" scadrà tra meno di 24 ore!";

                        // Usa un ID di sistema per le notifiche automatiche
                        notificationService.createNotification(
                            assigneeId,
                            task.getCreatedBy(), // Il creatore come sender
                            "taskDueSoon",
                            message,
                            task.getId(),
                            "task"
                        );

                        System.out.println("Notifica inviata a " + assignee.getDisplayName() + " per task: " + task.getTitle());
                    }
                }
            }
        }

        System.out.println("=== TaskSchedulerService: Controllo completato ===");
    }
}

