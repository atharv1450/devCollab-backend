package com.devCollab.dto;

import lombok.Data;
import java.util.List;

@Data
public class AiTaskBreakdownResponse {
    private String featureDescription;
    private List<AiGeneratedTask> tasks;
    private String rawResponse;

    @Data
    public static class AiGeneratedTask {
        private String title;
        private String description;
        private String priority;
        private String estimatedHours;
    }
}