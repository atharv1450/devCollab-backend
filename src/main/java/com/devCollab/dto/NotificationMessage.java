package com.devCollab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String type;
    private String message;
    private String username;
    private Long projectId;
    private LocalDateTime timestamp;
}
