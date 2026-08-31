package com.kripibot.service;

import com.kripibot.dto.*;
import com.kripibot.dto.gemini.GeminiDto;
import com.kripibot.model.Conversation;
import com.kripibot.model.Message;
import com.kripibot.model.Role;
import com.kripibot.repository.ConversationRepository;
import com.kripibot.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final GeminiService geminiService;

    public ChatService(ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       GeminiService geminiService) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.geminiService = geminiService;
    }

    @Transactional
    public ChatResponse processChat(ChatRequest request) {
        Conversation conversation;
        boolean isNew = (request.getConversationId() == null);

        if (isNew) {
            String title = geminiService.generateConversationTitle(request.getMessage(), request.getApiKey());
            conversation = new Conversation();
            conversation.setTitle(title);
            conversation.setModelUsed(request.getModel() != null ? request.getModel() : "gemini-3.6-flash");
            conversation = conversationRepository.save(conversation);
        } else {
            conversation = conversationRepository.findById(request.getConversationId())
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found with ID: " + request.getConversationId()));
        }

        // Retrieve existing history for context
        List<Message> history = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversation.getId());

        // Call Gemini API
        GeminiDto.Response geminiResponse = geminiService.generateContent(
                history,
                request.getMessage(),
                request.getModel(),
                request.getApiKey(),
                request.getSystemPrompt()
        );

        String botText = geminiResponse.getFirstText();
        Integer promptTokens = (geminiResponse.getUsageMetadata() != null) ? geminiResponse.getUsageMetadata().getPromptTokenCount() : 0;
        Integer candidateTokens = (geminiResponse.getUsageMetadata() != null) ? geminiResponse.getUsageMetadata().getCandidatesTokenCount() : 0;
        Integer totalTokens = (geminiResponse.getUsageMetadata() != null) ? geminiResponse.getUsageMetadata().getTotalTokenCount() : 0;

        // Persist User Message
        Message userMsg = new Message();
        userMsg.setConversation(conversation);
        userMsg.setRole(Role.USER);
        userMsg.setContent(request.getMessage());
        userMsg = messageRepository.save(userMsg);

        // Persist Model Message
        Message botMsg = new Message();
        botMsg.setConversation(conversation);
        botMsg.setRole(Role.MODEL);
        botMsg.setContent(botText);
        botMsg.setPromptTokens(promptTokens);
        botMsg.setCandidateTokens(candidateTokens);
        botMsg = messageRepository.save(botMsg);

        // Update conversation metadata
        if (request.getModel() != null) {
            conversation.setModelUsed(request.getModel());
        }
        conversationRepository.save(conversation);

        return new ChatResponse(
                conversation.getId(),
                userMsg.getId(),
                botMsg.getId(),
                conversation.getTitle(),
                botText,
                conversation.getModelUsed(),
                promptTokens,
                candidateTokens,
                totalTokens
        );
    }

    @Transactional(readOnly = true)
    public List<ConversationDto> getAllConversations() {
        return conversationRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConversationDto getConversation(UUID id) {
        Conversation conv = conversationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found with ID: " + id));

        List<MessageDto> messageDtos = conv.getMessages().stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList());

        return new ConversationDto(
                conv.getId(),
                conv.getTitle(),
                conv.getModelUsed(),
                conv.getCreatedAt(),
                conv.getUpdatedAt(),
                messageDtos.size(),
                "",
                messageDtos
        );
    }

    @Transactional
    public ConversationDto createConversation(String title, String model) {
        Conversation conv = new Conversation();
        conv.setTitle((title != null && !title.isBlank()) ? title.trim() : "New Chat");
        conv.setModelUsed((model != null && !model.isBlank()) ? model.trim() : "gemini-3.6-flash");
        conv = conversationRepository.save(conv);
        return toSummaryDto(conv);
    }

    @Transactional
    public void deleteConversation(UUID id) {
        if (!conversationRepository.existsById(id)) {
            throw new IllegalArgumentException("Conversation not found with ID: " + id);
        }
        conversationRepository.deleteById(id);
    }

    @Transactional
    public ConversationDto updateTitle(UUID id, String newTitle) {
        Conversation conv = conversationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found with ID: " + id));
        conv.setTitle(newTitle.trim());
        conv = conversationRepository.save(conv);
        return toSummaryDto(conv);
    }

    private ConversationDto toSummaryDto(Conversation conv) {
        String lastSnippet = "";
        if (conv.getMessages() != null && !conv.getMessages().isEmpty()) {
            Message last = conv.getMessages().get(conv.getMessages().size() - 1);
            lastSnippet = last.getContent().length() > 60
                    ? last.getContent().substring(0, 57) + "..."
                    : last.getContent();
        }

        return new ConversationDto(
                conv.getId(),
                conv.getTitle(),
                conv.getModelUsed(),
                conv.getCreatedAt(),
                conv.getUpdatedAt(),
                conv.getMessages() != null ? conv.getMessages().size() : 0,
                lastSnippet,
                null
        );
    }

    private MessageDto toMessageDto(Message msg) {
        return new MessageDto(
                msg.getId(),
                msg.getRole(),
                msg.getContent(),
                msg.getPromptTokens(),
                msg.getCandidateTokens(),
                msg.getCreatedAt()
        );
    }
}
