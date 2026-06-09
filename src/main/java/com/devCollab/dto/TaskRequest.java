package com.devCollab.dto;

import com.devCollab.entity.TaskPriority;
import com.devCollab.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Long assigneeId;
}
