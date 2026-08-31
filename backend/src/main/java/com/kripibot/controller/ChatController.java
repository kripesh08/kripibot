package com.kripibot.controller;

import com.kripibot.dto.ChatRequest;
import com.kripibot.dto.ChatResponse;
import com.kripibot.service.ChatService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request for conversation: {}", request.getConversationId());
        ChatResponse response = chatService.processChat(request);
        return ResponseEntity.ok(response);
    }
}
