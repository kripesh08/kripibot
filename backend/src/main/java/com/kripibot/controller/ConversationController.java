package com.kripibot.controller;

import com.kripibot.dto.ConversationDto;
import com.kripibot.dto.UpdateTitleRequest;
import com.kripibot.service.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private static final Logger log = LoggerFactory.getLogger(ConversationController.class);
    private final ChatService chatService;

    public ConversationController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<ConversationDto>> getAllConversations() {
        return ResponseEntity.ok(chatService.getAllConversations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationDto> getConversationById(@PathVariable UUID id) {
        return ResponseEntity.ok(chatService.getConversation(id));
    }

    @PostMapping
    public ResponseEntity<ConversationDto> createConversation(
            @RequestParam(required = false, defaultValue = "New Chat") String title,
            @RequestParam(required = false, defaultValue = "gemini-3.6-flash") String model
    ) {
        ConversationDto created = chatService.createConversation(title, model);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable UUID id) {
        chatService.deleteConversation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConversationDto> updateTitle(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTitleRequest request
    ) {
        return ResponseEntity.ok(chatService.updateTitle(id, request.getTitle()));
    }
}
