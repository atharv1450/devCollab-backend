package com.devCollab.service;

import com.devCollab.dto.ActivityLogResponse;
import com.devCollab.dto.NotificationMessage;
import com.devCollab.entity.ActivityLog;
import com.devCollab.entity.Project;
import com.devCollab.entity.User;
import com.devCollab.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityLogRepository activityLogRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void logAndNotify(Project project, User user,
                             String action, String entityType, String entityName) {
        // save to DB
        ActivityLog activityLog = ActivityLog.builder()
                .project(project)
                .user(user)
                .action(action)
                .entityType(entityType)
                .entityName(entityName)
                .build();
        activityLogRepository.save(activityLog);

        // send real-time notification via WebSocket
        NotificationMessage notification = new NotificationMessage(
                action,
                user.getUsername() + " " + action + " " + entityType + ": " + entityName,
                user.getUsername(),
                project.getId(),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/project/" + project.getId(),
                notification
        );

        log.info("Activity logged & notification sent: {}", notification.getMessage());
    }

    public List<ActivityLogResponse> getProjectActivity(Long projectId) {
        return activityLogRepository
                .findByProjectIdOrderByCreatedAtDesc(projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ActivityLogResponse toResponse(ActivityLog log) {
        ActivityLogResponse res = new ActivityLogResponse();
        res.setId(log.getId());
        res.setAction(log.getAction());
        res.setEntityType(log.getEntityType());
        res.setEntityName(log.getEntityName());
        res.setUsername(log.getUser().getUsername());
        res.setProjectId(log.getProject().getId());
        res.setCreatedAt(log.getCreatedAt());
        return res;
    }
}