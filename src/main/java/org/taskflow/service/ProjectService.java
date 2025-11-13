package org.taskflow.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.taskflow.dto.CollaboratorRequest;
import org.taskflow.dto.CollaboratorResponse;
import org.taskflow.dto.ProjectRequest;
import org.taskflow.dto.ProjectResponse;
import org.taskflow.dto.ProjectUpdateRequest;
import org.taskflow.exception.BadRequestException;
import org.taskflow.exception.ForbiddenException;
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
        Project project = projectRepository.getProjectById(id);
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

        // Imposta joinedAt per tutti i collaboratori passati nella request
        LocalDateTime now = LocalDateTime.now();
        for (Collaborator collaborator : collaborators) {
            if (collaborator.getJoinedAt() == null) {
                collaborator.setJoinedAt(now);
            }
        }

        boolean creatorAlreadyIn = collaborators.stream()
                .anyMatch(c -> c.getUserId().equals(creatorId));
        if (!creatorAlreadyIn) {
            Collaborator creatorCollaborator = new Collaborator();
            creatorCollaborator.setUserId(creatorId);
            creatorCollaborator.setRole("creator");
            creatorCollaborator.setJoinedAt(now);
            collaborators.add(creatorCollaborator);
        }

        newProject.setCollaborators(collaborators);
        newProject.setPhases(projectRequest.getPhases());
        newProject.setLabels(projectRequest.getLabels());
        newProject.setCreatedAt(now);
        newProject.setUpdatedAt(now);

        projectRepository.createProject(newProject);

        return toResponse(newProject);
    }

    public ProjectResponse update(ProjectUpdateRequest projectUpdateRequest, ObjectId userId, ObjectId id) {
        Project existing = projectRepository.getProjectById(id);
        if (existing == null) {
            throw new NotFoundException("Project not found");
        }

        if (!existing.getCreatorId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to update this project");
        }

        if (projectUpdateRequest.getTitle() != null) {
            existing.setTitle(projectUpdateRequest.getTitle());
        }
        if (projectUpdateRequest.getPhases() != null) {
            existing.setPhases(projectUpdateRequest.getPhases());
        }
        if (projectUpdateRequest.getLabels() != null) {
            existing.setLabels(projectUpdateRequest.getLabels());
        }
        existing.setUpdatedAt(LocalDateTime.now());
        projectRepository.updateProject(existing);
        return toResponse(existing);
    }

    public ProjectResponse addCollaborator(ObjectId projectId, ObjectId userId, CollaboratorRequest collaboratorRequest) {
        Project existing = projectRepository.getProjectById(projectId);
        if (existing == null) {
            throw new NotFoundException("Project not found");
        }

        if (!existing.getCreatorId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to modify this project");
        }
        Collaborator newCollaborator = new Collaborator();
        newCollaborator.setUserId(new ObjectId(collaboratorRequest.getUserId()));
        newCollaborator.setRole("member");
        newCollaborator.setJoinedAt(LocalDateTime.now());

        List<Collaborator> collaborators = existing.getCollaborators();
        if (collaborators == null) {
            collaborators = new ArrayList<>();
        }
        boolean alreadyExists = collaborators.stream()
                .anyMatch(c -> c.getUserId().equals(newCollaborator.getUserId()));

        if (alreadyExists) {
            throw new BadRequestException("Collaborator already exists in this project");
        }

        collaborators.add(newCollaborator);
        existing.setCollaborators(collaborators);
        existing.setUpdatedAt(LocalDateTime.now());
        projectRepository.updateProject(existing);

        return toResponse(existing);
    }

    public ProjectResponse removeCollaborator(ObjectId projectId, ObjectId userId, ObjectId collaboratorUserId) {
        Project existing = projectRepository.getProjectById(projectId);
        if (existing == null) {
            throw new NotFoundException("Project not found");
        }

        if (!existing.getCreatorId().equals(userId)) {
            throw new ForbiddenException("You are not allowed to modify this project");
        }

        if (collaboratorUserId.equals(userId)) {
            throw new BadRequestException("The project creator cannot remove themselves");
        }

        List<Collaborator> collaborators = existing.getCollaborators();
        if (collaborators != null) {
            collaborators.removeIf(c -> c.getUserId().equals(collaboratorUserId));
            existing.setCollaborators(collaborators);
            existing.setUpdatedAt(LocalDateTime.now());
            projectRepository.updateProject(existing);
        }

        return toResponse(existing);
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
        User user = userRepository.findById(userId);
        return user != null ? user.getDisplayName() : "Unknown";
    }


}
