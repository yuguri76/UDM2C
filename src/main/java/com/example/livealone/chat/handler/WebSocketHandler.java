package com.example.livealone.chat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();
    private final KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 새 웹소켓 세션 접속
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);
        log.info("New Session : " + session.getId());

        kafkaTemplate.send("chat", "New Session :" + session.getId());
    }

    /**
     * 웹소켓 세션 아웃
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){

        log.info("Exit Session :" + session.getId());
        try{
            CLIENTS.remove(session.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 소켓을 통해 메시지 수신 이벤트 발생
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        log.info("Read Message"+message);

        /**
         * Publish
         */
        kafkaTemplate.send("chat",session.getId()+" : "+message.getPayload());
    }

    public static ConcurrentHashMap<String,WebSocketSession> getClients(){
        return CLIENTS;
    }
}
