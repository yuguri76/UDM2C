package com.example.livealone.chat.controller;

import com.example.livealone.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ChatController")
public class ChatController {

    private final ChatService chatService;

    @KafkaListener(topics = "chat")
    public void listenGroupChat(String message) {
        log.info("Write Message " + message);
        chatService.write(message);
    }

}
