package org.taskflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.taskflow.dto.CollaboratorResponse;
import org.taskflow.dto.ProjectRequest;
import org.taskflow.dto.ProjectResponse;
import org.taskflow.exception.NotFoundException;
import org.taskflow.model.Collaborator;
import org.taskflow.model.Project;
import org.taskflow.model.User;
import org.taskflow.repository.ProjectRepository;
import org.taskflow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<ProjectResponse> getByUserId(ObjectId userId) {
        List<Project> projects = projectRepository.getProjectsByUserId(userId);
        return projects.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse create(ProjectRequest projectRequest, ObjectId creatorId) {
        Project newProject = new Project();
        newProject.setTitle(projectRequest.getTitle());
        newProject.setCreatorId(creatorId);

        List<Collaborator> collaborators = projectRequest.getCollaborators();

        if (collaborators == null) {
            collaborators = new ArrayList<>();
        }

        boolean creatorAlreadyIn = collaborators.stream()
                .anyMatch(c -> c.getUserId().equals(creatorId));
        if (!creatorAlreadyIn) {
            Collaborator creatorCollaborator = new Collaborator();
            creatorCollaborator.setUserId(creatorId);
            creatorCollaborator.setRole("creator");
            collaborators.add(creatorCollaborator);
        }

        newProject.setCollaborators(collaborators);
        newProject.setPhases(projectRequest.getPhases());
        newProject.setLabels(projectRequest.getLabels());
        newProject.setCreatedAt(LocalDateTime.now());
        newProject.setUpdatedAt(LocalDateTime.now());

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
                    if(user == null){
                        System.out.println("User not found!");
                    }
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
        User user = userRepository.findById(userId);
        return user != null ? user.getDisplayName() : "Unknown";
    }


}
