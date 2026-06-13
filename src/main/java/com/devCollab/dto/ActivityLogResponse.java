package com.devCollab.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActivityLogResponse {
    private Long id;
    private String action;
    private String entityType;
    private String entityName;
    private String username;
    private Long projectId;
    private LocalDateTime createdAt;
}
