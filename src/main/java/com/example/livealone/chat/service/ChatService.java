package com.example.livealone.chat.service;

import com.example.livealone.chat.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    /**
     * 현재 연결되어있는 세션에 모두 메시지 전송
     * message 포맷 ==> {유저닉네임} : {메시지}
     */
    public void write(String message) {

        ConcurrentHashMap<String, WebSocketSession> clients = WebSocketHandler.getClients();

        TextMessage text = new TextMessage(message);

        clients.forEach((key, value) -> {
            try {
                value.sendMessage(text);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });

    }

}
