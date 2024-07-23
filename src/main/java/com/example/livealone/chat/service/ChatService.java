package com.example.livealone.chat.service;

import com.example.livealone.chat.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
public class ChatService {

    /**
     * 현재 연결되어있는 세션에 모두 메시지 전송
     */
    public void write(String topic, String message) {

        ConcurrentHashMap<String, WebSocketSession> clients = WebSocketHandler.getClients();

        TextMessage text = new TextMessage(message);

        clients.entrySet().forEach(arg ->{
            try{
                arg.getValue().sendMessage(text);
            }catch (IOException e){
                e.printStackTrace();
            }
        });

    }

}
