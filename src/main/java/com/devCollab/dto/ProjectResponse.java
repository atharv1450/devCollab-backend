package com.devCollab.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private String ownerUsername;
    private int taskCount;
    private LocalDateTime createdAt;
}