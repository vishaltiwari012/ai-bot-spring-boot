package com.cw.learn_ai.controller;

import com.cw.learn_ai.payload.CricketResponse;
import com.cw.learn_ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<String> generateResponse(
            @RequestParam(value = "inputText") String inputText
    ) {
        String responseText = chatService.generateText(inputText);
        return ResponseEntity.ok(responseText);
    }

    @GetMapping("/stream")
    public Flux<String> streamResponse(
            @RequestParam(value = "inputText") String inputText
    ) {
        return chatService.streamResponse(inputText);
    }

    @GetMapping("/cricket-bot")
    public ResponseEntity<CricketResponse> getCricketResponse(
            @RequestParam(value = "inputText") String inputText
    ) throws IOException {
        CricketResponse cricketResponse = chatService.generateCricketResponse(inputText);
        return ResponseEntity.ok(cricketResponse);
    }

    @GetMapping("/images")
    public ResponseEntity<List<String>> generateImages(
            @RequestParam("description") String description,
            @RequestParam(value = "numberOfImages", required = false, defaultValue = "2") int numbers
    ) throws IOException {
        return ResponseEntity.ok(chatService.generateImages(description, numbers));
    }
}
