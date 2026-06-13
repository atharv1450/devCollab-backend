package com.devCollab.service;

import com.devCollab.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private String callClaude(String prompt) {
        try {
            String requestBody = objectMapper.writeValueAsString(new java.util.HashMap<>() {{
                put("model", "llama-3.3-70b-versatile");
                put("max_tokens", 1024);
                put("messages", List.of(new java.util.HashMap<>() {{
                    put("role", "user");
                    put("content", prompt);
                }}));
            }});

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            log.info("Groq API status: {}", response.statusCode());
            log.info("Groq API response: {}", response.body());

            JsonNode root = objectMapper.readTree(response.body());

            if (root.has("error")) {
                String errorMsg = root.path("error").path("message").asText();
                throw new RuntimeException("Groq API error: " + errorMsg);
            }

            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Groq API call failed", e);
            throw new RuntimeException("AI service unavailable: " + e.getMessage());
        }
    }
    public AiTaskBreakdownResponse breakdownFeature(AiTaskBreakdownRequest request) {
        String prompt = """
                You are a senior software engineer helping break down a feature into tasks.
                
                Feature Description: %s
                %s
                
                Break this down into 3-6 concrete development tasks.
                Respond in this exact JSON format (no markdown, just JSON):
                {
                  "tasks": [
                    {
                      "title": "task title",
                      "description": "what needs to be done",
                      "priority": "HIGH|MEDIUM|LOW",
                      "estimatedHours": "2-4"
                    }
                  ]
                }
                """.formatted(
                request.getFeatureDescription(),
                request.getProjectContext() != null
                        ? "Project Context: " + request.getProjectContext()
                        : ""
        );

        String aiResponse = callClaude(prompt);

        AiTaskBreakdownResponse response = new AiTaskBreakdownResponse();
        response.setFeatureDescription(request.getFeatureDescription());
        response.setRawResponse(aiResponse);

        try {
            String cleaned = aiResponse.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json\\n?", "").replaceAll("```\\n?", "").trim();
            }
            JsonNode root = objectMapper.readTree(cleaned);
            JsonNode tasksNode = root.path("tasks");

            List<AiTaskBreakdownResponse.AiGeneratedTask> tasks = new ArrayList<>();
            for (JsonNode taskNode : tasksNode) {
                AiTaskBreakdownResponse.AiGeneratedTask task =
                        new AiTaskBreakdownResponse.AiGeneratedTask();
                task.setTitle(taskNode.path("title").asText());
                task.setDescription(taskNode.path("description").asText());
                task.setPriority(taskNode.path("priority").asText());
                task.setEstimatedHours(taskNode.path("estimatedHours").asText());
                tasks.add(task);
            }
            response.setTasks(tasks);
        } catch (Exception e) {
            log.warn("Could not parse AI response as JSON, returning raw", e);
        }

        return response;
    }

    public AiPrSummaryResponse generatePrSummary(AiPrSummaryRequest request) {
        String prompt = """
                You are a senior developer writing a professional Pull Request description.
                
                Branch: %s
                Ticket/Feature: %s
                Code Changes:
                %s
                
                Write a professional PR description.
                Respond in this exact JSON format (no markdown, just JSON):
                {
                  "title": "concise PR title",
                  "summary": "what this PR does and why",
                  "typeOfChange": "feature|bugfix|refactor|hotfix",
                  "testingNotes": "how to test this change"
                }
                """.formatted(
                request.getBranchName() != null ? request.getBranchName() : "feature-branch",
                request.getTicketDescription() != null ? request.getTicketDescription() : "N/A",
                request.getCodeChanges()
        );

        String aiResponse = callClaude(prompt);

        AiPrSummaryResponse response = new AiPrSummaryResponse();
        response.setRawResponse(aiResponse);

        try {
            String cleaned = aiResponse.trim();
            if (cleaned.startsWith("```")) {
                cleaned = cleaned.replaceAll("```json\\n?", "").replaceAll("```\\n?", "").trim();
            }
            JsonNode root = objectMapper.readTree(cleaned);
            response.setTitle(root.path("title").asText());
            response.setSummary(root.path("summary").asText());
            response.setTypeOfChange(root.path("typeOfChange").asText());
            response.setTestingNotes(root.path("testingNotes").asText());
        } catch (Exception e) {
            log.warn("Could not parse AI response as JSON, returning raw", e);
        }

        return response;
    }
}
