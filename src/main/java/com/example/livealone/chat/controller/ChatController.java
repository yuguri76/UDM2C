package com.example.livealone.chat.controller;

import com.example.livealone.chat.service.ChatService;
import com.example.livealone.global.dto.SocketMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ChatController")
public class ChatController {

    private final ChatService chatService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private final ConcurrentMap<String, Integer> roomParticipants = new ConcurrentHashMap<>();

    @MessageMapping("/session")
    @SendToUser("/queue/reply")
    public String getAuthRequest(SocketMessageDto socketMessageDto) throws JsonProcessingException {
        return chatService.createSessionReply(socketMessageDto);
    }

    @MessageMapping("/send")
    public void getMessageRequest(SocketMessageDto chat) throws JsonProcessingException {
        String kafkaMessage = chatService.createKafkaMessage(chat);
        kafkaTemplate.send("chat", kafkaMessage);
    }

    @KafkaListener(topics = "chat")
    public void listenGroupChat(String message) {
        SocketMessageDto socketMessageDto = chatService.write(message);
        messagingTemplate.convertAndSend("/queue/message", socketMessageDto);
    }
}
