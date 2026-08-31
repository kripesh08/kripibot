package com.kripibot.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ConversationDto {
    private UUID id;
    private String title;
    private String modelUsed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int messageCount;
    private String lastMessageSnippet;
    private List<MessageDto> messages;

    public ConversationDto() {}

    public ConversationDto(UUID id, String title, String modelUsed, LocalDateTime createdAt, LocalDateTime updatedAt, int messageCount, String lastMessageSnippet, List<MessageDto> messages) {
        this.id = id;
        this.title = title;
        this.modelUsed = modelUsed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messageCount = messageCount;
        this.lastMessageSnippet = lastMessageSnippet;
        this.messages = messages;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getModelUsed() { return modelUsed; }
    public void setModelUsed(String modelUsed) { this.modelUsed = modelUsed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public int getMessageCount() { return messageCount; }
    public void setMessageCount(int messageCount) { this.messageCount = messageCount; }
    public String getLastMessageSnippet() { return lastMessageSnippet; }
    public void setLastMessageSnippet(String lastMessageSnippet) { this.lastMessageSnippet = lastMessageSnippet; }
    public List<MessageDto> getMessages() { return messages; }
    public void setMessages(List<MessageDto> messages) { this.messages = messages; }
}
