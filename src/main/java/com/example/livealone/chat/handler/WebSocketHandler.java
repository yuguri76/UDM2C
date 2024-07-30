package com.example.livealone.chat.handler;

import com.example.livealone.chat.dto.ChatDto;
import com.example.livealone.global.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    /**
     * 새 웹소켓 세션 접속
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);
        log.info("New Sesssion :"+session.getId());
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
            log.error(e.getMessage());
        }

    }

    /**
     * 소켓을 통해 메시지 수신 이벤트 발생
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        log.info("Read Message :"+message.getPayload());

        String jsonMessage = createJsonMessage(message.getPayload());

        kafkaTemplate.send("chat",jsonMessage);
    }

    public static ConcurrentHashMap<String,WebSocketSession> getClients(){
        return CLIENTS;
    }


    private String createJsonMessage(String payload){
        try{
            ChatDto readChat = objectMapper.readValue(payload, ChatDto.class);

            if(Objects.equals(readChat.getType(), "AUTH")){
                // 유저이름 리턴
                String token = readChat.getMessage().replace("Bearer ","");

                String isValidToken = jwtService.isValidToken(token);
                if(!Objects.equals(isValidToken, "Valid")){
                    ChatDto failedMessage = new ChatDto("FAILED","",isValidToken);
                    return objectMapper.writeValueAsString(failedMessage);
                }

                Claims claims = jwtService.getClaims(token);

                String nickname = claims.get("nickname",String.class);

                ChatDto chatDto = new ChatDto("AUTH",nickname,"");
                return objectMapper.writeValueAsString(chatDto);
            }
            return payload;
        }catch (IOException e){
            log.info(e.getMessage());
            return null;
        }
    }
}
