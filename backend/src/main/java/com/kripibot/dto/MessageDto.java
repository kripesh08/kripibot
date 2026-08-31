package com.kripibot.dto;

import com.kripibot.model.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public class MessageDto {
    private UUID id;
    private Role role;
    private String content;
    private Integer promptTokens;
    private Integer candidateTokens;
    private LocalDateTime createdAt;

    public MessageDto() {}

    public MessageDto(UUID id, Role role, String content, Integer promptTokens, Integer candidateTokens, LocalDateTime createdAt) {
        this.id = id;
        this.role = role;
        this.content = content;
        this.promptTokens = promptTokens;
        this.candidateTokens = candidateTokens;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getPromptTokens() { return promptTokens; }
    public void setPromptTokens(Integer promptTokens) { this.promptTokens = promptTokens; }
    public Integer getCandidateTokens() { return candidateTokens; }
    public void setCandidateTokens(Integer candidateTokens) { this.candidateTokens = candidateTokens; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
