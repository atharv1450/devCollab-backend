package com.devCollab.service;

import com.devCollab.dto.TaskRequest;
import com.devCollab.dto.TaskResponse;
import com.devCollab.entity.*;
import com.devCollab.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(Long projectId, TaskRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .project(project)
                .assignee(assignee)
                .build();

        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getTasksByProject(Long projectId, TaskStatus status) {
        List<Task> tasks = status != null
                ? taskRepository.findByProjectIdAndStatus(projectId, status)
                : taskRepository.findByProjectId(projectId);
        return tasks.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignee(assignee);
        }

        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        res.setStatus(task.getStatus());
        res.setPriority(task.getPriority());
        res.setProjectId(task.getProject().getId());
        res.setProjectName(task.getProject().getName());
        if (task.getAssignee() != null) {
            res.setAssigneeUsername(task.getAssignee().getUsername());
        }
        res.setCreatedAt(task.getCreatedAt());
        res.setUpdatedAt(task.getUpdatedAt());
        return res;
    }
}
