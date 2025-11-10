package org.taskflow.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.taskflow.model.Project;
@ApplicationScoped
public class ProjectRepository implements PanacheMongoRepository<Project> {
}
