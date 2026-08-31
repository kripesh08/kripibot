package com.kripibot.dto.gemini;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class GeminiDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Request {
        @JsonProperty("system_instruction")
        private Content systemInstruction;
        private List<Content> contents = new ArrayList<>();
        @JsonProperty("generationConfig")
        private GenerationConfig generationConfig;

        public Request() {}

        public Request(Content systemInstruction, List<Content> contents, GenerationConfig generationConfig) {
            this.systemInstruction = systemInstruction;
            this.contents = contents != null ? contents : new ArrayList<>();
            this.generationConfig = generationConfig;
        }

        public Content getSystemInstruction() { return systemInstruction; }
        public void setSystemInstruction(Content systemInstruction) { this.systemInstruction = systemInstruction; }
        public List<Content> getContents() { return contents; }
        public void setContents(List<Content> contents) { this.contents = contents; }
        public GenerationConfig getGenerationConfig() { return generationConfig; }
        public void setGenerationConfig(GenerationConfig generationConfig) { this.generationConfig = generationConfig; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {
        private String role;
        private List<Part> parts = new ArrayList<>();

        public Content() {}

        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts != null ? parts : new ArrayList<>();
        }

        public static Content of(String role, String text) {
            Content c = new Content();
            c.setRole(role);
            c.setParts(List.of(new Part(text)));
            return c;
        }

        public static Content system(String text) {
            Content c = new Content();
            c.setParts(List.of(new Part(text)));
            return c;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public List<Part> getParts() { return parts; }
        public void setParts(List<Part> parts) { this.parts = parts; }
    }

    public static class Part {
        private String text;

        public Part() {}
        public Part(String text) { this.text = text; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GenerationConfig {
        private Double temperature;
        private Double topP;
        private Integer topK;
        private Integer maxOutputTokens;

        public GenerationConfig() {}

        public GenerationConfig(Double temperature, Double topP, Integer topK, Integer maxOutputTokens) {
            this.temperature = temperature;
            this.topP = topP;
            this.topK = topK;
            this.maxOutputTokens = maxOutputTokens;
        }

        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Double getTopP() { return topP; }
        public void setTopP(Double topP) { this.topP = topP; }
        public Integer getTopK() { return topK; }
        public void setTopK(Integer topK) { this.topK = topK; }
        public Integer getMaxOutputTokens() { return maxOutputTokens; }
        public void setMaxOutputTokens(Integer maxOutputTokens) { this.maxOutputTokens = maxOutputTokens; }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private List<Candidate> candidates;
        private UsageMetadata usageMetadata;
        private ErrorInfo error;

        public Response() {}

        public List<Candidate> getCandidates() { return candidates; }
        public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }
        public UsageMetadata getUsageMetadata() { return usageMetadata; }
        public void setUsageMetadata(UsageMetadata usageMetadata) { this.usageMetadata = usageMetadata; }
        public ErrorInfo getError() { return error; }
        public void setError(ErrorInfo error) { this.error = error; }

        public String getFirstText() {
            if (candidates != null && !candidates.isEmpty()) {
                Candidate candidate = candidates.get(0);
                if (candidate.getContent() != null && candidate.getContent().getParts() != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Part part : candidate.getContent().getParts()) {
                        if (part.getText() != null) {
                            sb.append(part.getText());
                        }
                    }
                    return sb.toString();
                }
            }
            return "";
        }
    }

    public static class Candidate {
        private Content content;
        private String finishReason;

        public Candidate() {}
        public Content getContent() { return content; }
        public void setContent(Content content) { this.content = content; }
        public String getFinishReason() { return finishReason; }
        public void setFinishReason(String finishReason) { this.finishReason = finishReason; }
    }

    public static class UsageMetadata {
        private Integer promptTokenCount;
        private Integer candidatesTokenCount;
        private Integer totalTokenCount;

        public UsageMetadata() {}
        public Integer getPromptTokenCount() { return promptTokenCount; }
        public void setPromptTokenCount(Integer promptTokenCount) { this.promptTokenCount = promptTokenCount; }
        public Integer getCandidatesTokenCount() { return candidatesTokenCount; }
        public void setCandidatesTokenCount(Integer candidatesTokenCount) { this.candidatesTokenCount = candidatesTokenCount; }
        public Integer getTotalTokenCount() { return totalTokenCount; }
        public void setTotalTokenCount(Integer totalTokenCount) { this.totalTokenCount = totalTokenCount; }
    }

    public static class ErrorInfo {
        private int code;
        private String message;
        private String status;

        public ErrorInfo() {}
        public int getCode() { return code; }
        public void setCode(int code) { this.code = code; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
