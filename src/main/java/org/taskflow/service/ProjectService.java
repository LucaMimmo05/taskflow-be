package org.taskflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.taskflow.dto.CollaboratorResponse;
import org.taskflow.dto.ProjectRequest;
import org.taskflow.dto.ProjectResponse;
import org.taskflow.exception.BadRequestException;
import org.taskflow.model.Project;
import org.taskflow.model.User;
import org.taskflow.repository.ProjectRepository;
import org.taskflow.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;

    @Inject
    UserRepository userRepository;

    public ProjectResponse findById(ObjectId id) {
        Project project = projectRepository.find("id", id).firstResult();
        if (project == null) {
            return null;
        }
        return toResponse(project);
    }

    public ProjectResponse create(ProjectRequest project, ObjectId creatorId) {
        Project newProject = new Project();
        newProject.setTitle(project.getTitle());
        newProject.setCreatorId(creatorId);
        newProject.setCollaborators(project.getCollaborators());
        newProject.setPhases(project.getPhases());
        newProject.setLabels(project.getLabels());
        newProject.setCreatedAt(java.time.LocalDateTime.now());
        newProject.setUpdatedAt(java.time.LocalDateTime.now());

        projectRepository.createProject(newProject);
        return toResponse(newProject);
    }

    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId().toString());
        response.setTitle(project.getTitle());
        response.setCreatorName(findUserNameById(project.getCreatorId()));
        response.setPhases(project.getPhases());
        response.setLabels(project.getLabels());

        List<CollaboratorResponse> collabResp = project.getCollaborators().stream()
                .map(c -> {
                    User user = userRepository.findById(c.getUserId());
                    CollaboratorResponse cr = new CollaboratorResponse();
                    cr.setUserId(c.getUserId().toString());
                    cr.setDisplayName(user != null ? user.getDisplayName() : "Unknown");
                    cr.setRole(c.getRole());
                    return cr;
                })
                .collect(Collectors.toList());
        response.setCollaborators(collabResp);

        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        return response;
    }



    public String findUserNameById(ObjectId userId) {
        User user = userRepository.find("id", userId).firstResult();
        return user != null ? user.getDisplayName() : "Unknown";
    }

}
