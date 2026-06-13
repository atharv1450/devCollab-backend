package com.devCollab.controller;

import com.devCollab.dto.ActivityLogResponse;
import com.devCollab.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<ActivityLogResponse>> getActivity(
            @PathVariable Long projectId) {
        return ResponseEntity.ok(activityService.getProjectActivity(projectId));
    }
}