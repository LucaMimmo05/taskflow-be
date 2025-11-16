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
}
