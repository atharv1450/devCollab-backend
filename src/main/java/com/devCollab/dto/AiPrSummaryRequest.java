package com.devCollab.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AiPrSummaryRequest {
    @NotBlank
    private String codeChanges;
    private String ticketDescription;
    private String branchName;
}
