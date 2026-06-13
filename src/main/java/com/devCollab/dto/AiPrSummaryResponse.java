package com.devCollab.dto;

import lombok.Data;

@Data
public class AiPrSummaryResponse {
    private String title;
    private String summary;
    private String typeOfChange;
    private String testingNotes;
    private String rawResponse;
}