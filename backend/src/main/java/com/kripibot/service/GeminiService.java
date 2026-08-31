package com.kripibot.service;

import com.kripibot.config.GeminiProperties;
import com.kripibot.dto.gemini.GeminiDto;
import com.kripibot.model.Message;
import com.kripibot.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private final GeminiProperties properties;
    private final RestClient restClient;

    public GeminiService(GeminiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder().build();
    }

    public GeminiDto.Response generateContent(
            List<Message> history,
            String newUserPrompt,
            String requestedModel,
            String customApiKey,
            String customSystemPrompt
    ) {
        String apiKey = resolveApiKey(customApiKey);
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Google Gemini API Key is missing. Please configure it in the application settings or set GEMINI_API_KEY.");
        }

        String rawModel = (requestedModel != null && !requestedModel.trim().isEmpty()) 
                ? requestedModel.trim() 
                : properties.getModel();
        String model = normalizeModel(rawModel);

        String systemPrompt = (customSystemPrompt != null && !customSystemPrompt.trim().isEmpty())
                ? customSystemPrompt.trim()
                : properties.getDefaultSystemPrompt();

        GeminiDto.Request request = buildGeminiRequest(history, newUserPrompt, systemPrompt);

        String url = String.format("%s/models/%s:generateContent?key=%s",
                properties.getBaseUrl(), model, apiKey);

        log.info("Sending request to Gemini model: {} with {} historical messages", model, (history != null ? history.size() : 0));

        try {
            GeminiDto.Response response = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiDto.Response.class);

            if (response == null || response.getFirstText().isEmpty()) {
                throw new RuntimeException("Received empty response from Gemini API.");
            }

            return response;
        } catch (HttpClientErrorException ex) {
            log.error("Gemini API Client Error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new RuntimeException("Gemini API Error (" + ex.getStatusCode() + "): " + ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            log.error("Gemini API communication failed", ex);
            throw new RuntimeException("Failed to communicate with Gemini API: " + ex.getMessage(), ex);
        }
    }

    public String generateConversationTitle(String firstPrompt, String customApiKey) {
        try {
            String apiKey = resolveApiKey(customApiKey);
            if (apiKey == null || apiKey.trim().isEmpty()) {
                return truncateTitle(firstPrompt);
            }

            String model = properties.getModel() != null ? properties.getModel() : "gemini-2.5-flash";
            String url = String.format("%s/models/%s:generateContent?key=%s",
                    properties.getBaseUrl(), model, apiKey);

            GeminiDto.Request request = new GeminiDto.Request();
            request.setSystemInstruction(GeminiDto.Content.system("Generate a 3-5 word concise, catchy title for a chat that starts with the user's message. Return ONLY the title text, nothing else."));
            request.setContents(List.of(GeminiDto.Content.of("user", firstPrompt)));
            request.setGenerationConfig(new GeminiDto.GenerationConfig(0.3, 0.95, null, 30));

            GeminiDto.Response response = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiDto.Response.class);

            if (response != null && !response.getFirstText().isEmpty()) {
                String title = response.getFirstText().replaceAll("[\"\n\r]", "").trim();
                return title.isEmpty() ? truncateTitle(firstPrompt) : title;
            }
        } catch (Exception ex) {
            log.warn("Failed to generate AI title: {}", ex.getMessage());
        }
        return truncateTitle(firstPrompt);
    }

    private GeminiDto.Request buildGeminiRequest(List<Message> history, String newUserPrompt, String systemPrompt) {
        List<GeminiDto.Content> contents = new ArrayList<>();

        if (history != null) {
            for (Message msg : history) {
                String role = (msg.getRole() == Role.USER) ? "user" : "model";
                contents.add(GeminiDto.Content.of(role, msg.getContent()));
            }
        }

        // Add the current user prompt
        contents.add(GeminiDto.Content.of("user", newUserPrompt));

        GeminiDto.Request request = new GeminiDto.Request();
        request.setContents(contents);
        request.setGenerationConfig(new GeminiDto.GenerationConfig(0.7, 0.95, 40, 4096));

        if (systemPrompt != null && !systemPrompt.isBlank()) {
            request.setSystemInstruction(GeminiDto.Content.system(systemPrompt));
        }

        return request;
    }

    private String normalizeModel(String rawModel) {
        if (rawModel == null || rawModel.trim().isEmpty()) {
            return "gemini-3.6-flash";
        }
        String model = rawModel.trim();
        if (model.startsWith("models/")) {
            model = model.substring(7);
        }
        if ("gemini-1.5-flash".equalsIgnoreCase(model) 
                || "gemini-2.0-flash".equalsIgnoreCase(model) 
                || "gemini-2.5-flash".equalsIgnoreCase(model)
                || "gemini-flash".equalsIgnoreCase(model)) {
            return "gemini-3.6-flash";
        }
        if ("gemini-1.5-pro".equalsIgnoreCase(model) || "gemini-2.5-pro".equalsIgnoreCase(model)) {
            return "gemini-3.7-flash";
        }
        return model;
    }

    private String resolveApiKey(String customApiKey) {
        if (customApiKey != null && !customApiKey.trim().isEmpty()) {
            return customApiKey.trim();
        }
        return properties.getKey();
    }

    private String truncateTitle(String prompt) {
        if (prompt == null) return "New Conversation";
        String clean = prompt.trim().replaceAll("\\s+", " ");
        if (clean.length() <= 35) return clean;
        return clean.substring(0, 32) + "...";
    }
}
