package com.game.fish.controller;

import com.game.fish.service.ChatGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general")
public class ChatController {

    @Autowired
    private ChatGeneralService chatGeneralService;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String quest){
        String response = chatGeneralService.chatService(quest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/interact")
    public ResponseEntity<String> interact(@RequestBody String quest) {
        String response = chatGeneralService.matchKeywords(quest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-image")
    public ResponseEntity<String> generateImage(@RequestBody String quest) {
        String response = chatGeneralService.generateImage(quest);
        return ResponseEntity.ok(response);
    }
}
