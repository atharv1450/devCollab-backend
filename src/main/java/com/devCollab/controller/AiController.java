package com.devCollab.controller;

import com.devCollab.dto.*;
import com.devCollab.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/breakdown")
    public ResponseEntity<AiTaskBreakdownResponse> breakdownFeature(
            @Valid @RequestBody AiTaskBreakdownRequest request) {
        return ResponseEntity.ok(aiService.breakdownFeature(request));
    }

    @PostMapping("/pr-summary")
    public ResponseEntity<AiPrSummaryResponse> generatePrSummary(
            @Valid @RequestBody AiPrSummaryRequest request) {
        return ResponseEntity.ok(aiService.generatePrSummary(request));
    }
}
