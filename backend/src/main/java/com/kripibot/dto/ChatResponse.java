package com.kripibot.dto;

import java.util.UUID;

public class ChatResponse {
    private UUID conversationId;
    private UUID userMessageId;
    private UUID botMessageId;
    private String conversationTitle;
    private String response;
    private String model;
    private Integer promptTokens;
    private Integer candidateTokens;
    private Integer totalTokens;

    public ChatResponse() {}

    public ChatResponse(UUID conversationId, UUID userMessageId, UUID botMessageId, String conversationTitle, String response, String model, Integer promptTokens, Integer candidateTokens, Integer totalTokens) {
        this.conversationId = conversationId;
        this.userMessageId = userMessageId;
        this.botMessageId = botMessageId;
        this.conversationTitle = conversationTitle;
        this.response = response;
        this.model = model;
        this.promptTokens = promptTokens;
        this.candidateTokens = candidateTokens;
        this.totalTokens = totalTokens;
    }

    public UUID getConversationId() { return conversationId; }
    public void setConversationId(UUID conversationId) { this.conversationId = conversationId; }
    public UUID getUserMessageId() { return userMessageId; }
    public void setUserMessageId(UUID userMessageId) { this.userMessageId = userMessageId; }
    public UUID getBotMessageId() { return botMessageId; }
    public void setBotMessageId(UUID botMessageId) { this.botMessageId = botMessageId; }
    public String getConversationTitle() { return conversationTitle; }
    public void setConversationTitle(String conversationTitle) { this.conversationTitle = conversationTitle; }
    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getPromptTokens() { return promptTokens; }
    public void setPromptTokens(Integer promptTokens) { this.promptTokens = promptTokens; }
    public Integer getCandidateTokens() { return candidateTokens; }
    public void setCandidateTokens(Integer candidateTokens) { this.candidateTokens = candidateTokens; }
    public Integer getTotalTokens() { return totalTokens; }
    public void setTotalTokens(Integer totalTokens) { this.totalTokens = totalTokens; }
}
