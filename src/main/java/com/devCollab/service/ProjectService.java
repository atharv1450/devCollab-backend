package com.devCollab.service;

import com.devCollab.dto.ProjectRequest;
import com.devCollab.dto.ProjectResponse;
import com.devCollab.entity.Project;
import com.devCollab.entity.User;
import com.devCollab.repository.ProjectRepository;
import com.devCollab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ProjectResponse createProject(ProjectRequest request) {
        User owner = getCurrentUser();
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(owner)
                .build();
        return toResponse(projectRepository.save(project));
    }

    public List<ProjectResponse> getMyProjects() {
        User owner = getCurrentUser();
        return projectRepository.findByOwner(owner)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getProject(Long id) {
        return toResponse(projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found")));
    }

    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        return toResponse(projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    private ProjectResponse toResponse(Project project) {
        ProjectResponse res = new ProjectResponse();
        res.setId(project.getId());
        res.setName(project.getName());
        res.setDescription(project.getDescription());
        res.setOwnerUsername(project.getOwner().getUsername());
        res.setTaskCount(project.getTasks() != null ? project.getTasks().size() : 0);
        res.setCreatedAt(project.getCreatedAt());
        return res;
    }
}