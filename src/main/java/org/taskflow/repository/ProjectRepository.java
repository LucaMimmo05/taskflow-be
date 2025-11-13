package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import org.taskflow.dto.ProjectResponse;
import org.taskflow.model.Project;

import java.util.List;


@ApplicationScoped
public class ProjectRepository implements PanacheMongoRepository<Project> {
    public void createProject(Project project) {
        persist(project);
    }

    public List<Project> getProjectsByUserId(ObjectId userId) {
        return find("creatorId", userId).list();
    }
}
