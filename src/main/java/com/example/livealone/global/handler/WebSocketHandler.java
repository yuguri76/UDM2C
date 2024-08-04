package com.example.livealone.global.handler;

import com.example.livealone.broadcast.service.BroadcastService;
import com.example.livealone.chat.service.ChatService;
import com.example.livealone.global.dto.SocketMessageDto;
import com.example.livealone.global.exception.CustomException;
import com.example.livealone.global.security.JwtService;
import com.example.livealone.user.dto.ReissueRequestDto;
import com.example.livealone.user.dto.TokenResponseDto;
import com.example.livealone.user.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static com.example.livealone.global.entity.SocketMessageType.*;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "WebSocket Handler")
public class WebSocketHandler extends TextWebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final AuthService authService;

    private final ChatService chatService;
    private final BroadcastService broadcastService;

    /**
     * 새 웹소켓 세션 접속
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);
        log.info("New Sesssion :" + session.getId());
    }

    /**
     * 웹소켓 세션 아웃
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        log.info("Exit Session :" + session.getId());
        try {
            CLIENTS.remove(session.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 소켓을 통해 메시지 수신 이벤트 발생
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SocketMessageDto socketMessageDto = getMessageType(message.getPayload());
        log.info("handleTextMessage :",message.getPayload());
        String jsonMessage;
        switch (socketMessageDto.getType()) {
            case REQUEST_AUTH -> {
                jsonMessage = getAuthMessage(session, socketMessageDto);

                chatService.responseDirectMessageToSocekt(session,jsonMessage);
            }
            case REQUEST_REFRESH -> {
                try{
                    String refresh = socketMessageDto.getMessage() == null? "invalid refreshToken" : socketMessageDto.getMessage();

                    TokenResponseDto tokenResponseDto = authService.reissueAccessToken(new ReissueRequestDto(refresh));
                    jsonMessage = getRefreshMessage(socketMessageDto, tokenResponseDto);

                    chatService.responseDirectMessageToSocekt(session,jsonMessage);
                }catch (CustomException e){
                    SocketMessageDto anonymousUserMessage = new SocketMessageDto(ANONYMOUS_USER, "back-server", e.getMessage());
                    jsonMessage = objectMapper.writeValueAsString(anonymousUserMessage);
                    chatService.responseDirectMessageToSocekt(session,jsonMessage);
                }
            }
            case CHAT_MESSAGE -> {
                jsonMessage = message.getPayload();

                kafkaTemplate.send("chat", jsonMessage);
            }
            case REQUEST_CHAT_INIT -> {
                // kafka로 메시지를 보내지 않고, 접속한 세션에게 바로 전송

                chatService.writeInitMessage(session);
            }
            case ERROR -> {
                // 아직은 안쓰고 일단 비워뒀습니다. (에러메시지 처리)
                jsonMessage = getErrorMessage(session, socketMessageDto);

            }
            case BROADCAST -> {
                broadcastService.requestStreamKey();
            }
        }
    }

    public static void broadcast(TextMessage message) {
        System.out.println(message);
        CLIENTS.forEach((key, session) -> {
            try {
                session.sendMessage(message);
            } catch (IOException ignored) {
            }
        });
    }

    public static ConcurrentHashMap<String, WebSocketSession> getClients() {
        return CLIENTS;
    }

    private SocketMessageDto getMessageType(String payload) {
        try {
            return objectMapper.readValue(payload, SocketMessageDto.class);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            return new SocketMessageDto(ERROR, "back-server", e.getMessage());
        }
    }

    /**
     * AUTH메시지 생성 (Chat)
     */
    private String getAuthMessage(WebSocketSession session, SocketMessageDto socketMessageDto) throws JsonProcessingException {
        try {
            String token = socketMessageDto.getMessage().replace("Bearer ", "");

            String isValidToken = jwtService.isValidToken(token);
            if (!Objects.equals(isValidToken, "Valid")) {
                SocketMessageDto failedMessage = new SocketMessageDto(INVALID_TOKEN, "back-server", isValidToken);
                return objectMapper.writeValueAsString(failedMessage);
            }

            Claims claims = jwtService.getClaims(token);
            String nickname = claims.get("nickname", String.class);

            SocketMessageDto newMessage = new SocketMessageDto(RESPONSE_AUTH, nickname, session.getId());
            return objectMapper.writeValueAsString(newMessage);
        } catch (IOException e) {
            log.info(e.getMessage());
            SocketMessageDto errorMessage = new SocketMessageDto(FAILED, "back-server", e.getMessage());
            return objectMapper.writeValueAsString(errorMessage);
        }
    }

    private String getRefreshMessage(SocketMessageDto socketMessageDto,TokenResponseDto tokenResponseDto) throws JsonProcessingException {
        try{

            SocketMessageDto refreshMessage = new SocketMessageDto(RESPONSE_REFRESH,socketMessageDto.getMessenger(),
                    objectMapper.writeValueAsString(tokenResponseDto));
            log.info("리프레쉬 요청"+tokenResponseDto.getAccess());
            return objectMapper.writeValueAsString(refreshMessage);
        }catch (JsonProcessingException e){
            log.info(e.getMessage());
            SocketMessageDto errorMessage = new SocketMessageDto(FAILED,"back-server",e.getMessage());
            return objectMapper.writeValueAsString(errorMessage);
        }
    }

    /**
     * 서버에서 발생하는 것이 아닌 프론트 혹은 외부에서 온 에러 메시지
     */
    private String getErrorMessage(WebSocketSession session, SocketMessageDto socketMessageDto) throws JsonProcessingException {
        try {
            return objectMapper.writeValueAsString(socketMessageDto);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            SocketMessageDto error = new SocketMessageDto(ERROR, "back-server", e.getMessage());
            return objectMapper.writeValueAsString(error);
        }
    }
}
