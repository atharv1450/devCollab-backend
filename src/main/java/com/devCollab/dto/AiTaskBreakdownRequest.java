package com.devCollab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiTaskBreakdownRequest {
    @NotBlank
    private String featureDescription;
    private String projectContext;
}
