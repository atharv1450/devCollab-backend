package com.devCollab.controller;

import com.devCollab.dto.TaskRequest;
import com.devCollab.dto.TaskResponse;
import com.devCollab.entity.TaskStatus;
import com.devCollab.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId,
                                                   @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(projectId, request));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Long projectId,
                                                       @RequestParam(required = false) TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, status));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long projectId,
                                                   @PathVariable Long taskId,
                                                   @Valid @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId,
                                           @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
