package com.devCollab.dto;

import com.devCollab.entity.TaskPriority;
import com.devCollab.entity.TaskStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String assigneeUsername;
    private Long projectId;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}