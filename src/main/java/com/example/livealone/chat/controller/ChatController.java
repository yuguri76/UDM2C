package com.example.livealone.chat.controller;

import com.example.livealone.chat.service.ChatService;
import com.example.livealone.global.dto.SocketMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


@RestController
@RequiredArgsConstructor
@Slf4j(topic = "ChatController")
public class ChatController {

    private final ChatService chatService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    private final Queue<SocketMessageDto> messageQueue = new ConcurrentLinkedQueue<>();

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

    @KafkaListener(topics ="viewer")
    public  void listenChannelInbounds(String message){

        SocketMessageDto socketMessageDto = chatService.setTotalViewerCountInbound(message);
        messageQueue.add(socketMessageDto);
        processMessageQueue();
    }

    private void processMessageQueue(){
        while (!messageQueue.isEmpty()) {
            SocketMessageDto message = messageQueue.poll();
            if(message != null){
                messagingTemplate.convertAndSend("/queue/viewer", message);
            }
        }
    }

}
