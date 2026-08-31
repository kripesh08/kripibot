package com.kripibot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public class ChatRequest {

    private UUID conversationId;

    @NotBlank(message = "Message content cannot be blank")
    private String message;

    private String model;
    private String apiKey;
    private String systemPrompt;

    public ChatRequest() {}

    public ChatRequest(UUID conversationId, String message, String model, String apiKey, String systemPrompt) {
        this.conversationId = conversationId;
        this.message = message;
        this.model = model;
        this.apiKey = apiKey;
        this.systemPrompt = systemPrompt;
    }

    public UUID getConversationId() { return conversationId; }
    public void setConversationId(UUID conversationId) { this.conversationId = conversationId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public String getSystemPrompt() { return systemPrompt; }
    public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }
}
