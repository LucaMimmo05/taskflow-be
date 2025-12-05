package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.taskflow.model.Task;

import java.util.List;

@ApplicationScoped
public class TaskRepository implements PanacheMongoRepository<Task> {

    public List<Task> getTasksByProjectId(ObjectId projectId) {
        return find("projectId", projectId).list();
    }

    public void createTask(Task task) {
        persist(task);
    }

    public Task findByTaskId(ObjectId taskId) {
        return findById(taskId);
    }

    public void updateTask(Task task) {
        update(task);
    }

    public void deleteTask(ObjectId taskId) {
        deleteById(taskId);
    }

    public void deleteTasksByProjectId(ObjectId projectId) {
        delete("projectId", projectId);
    }

    public List<Task> findTasksDueSoon(java.time.LocalDateTime now, java.time.LocalDateTime in24Hours) {
        return find("dueDate >= ?1 and dueDate <= ?2", now, in24Hours).list();
    }
}
