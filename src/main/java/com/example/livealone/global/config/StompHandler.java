package com.example.livealone.global.config;

import com.example.livealone.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert headerAccessor != null;
        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 성공 시

            String roomId = String.valueOf(headerAccessor.getFirstNativeHeader("RoomId"));
            String sessionId = headerAccessor.getSessionId();
            log.info("Channel inbound : {}",roomId);
            chatService.sendChannelInboundSessionMessage(roomId,sessionId);

        }
        else if(headerAccessor.getCommand() == StompCommand.DISCONNECT){

            String roomId = String.valueOf(headerAccessor.getFirstNativeHeader("RoomId"));
            String sessionId =  headerAccessor.getSessionId();
            if(Objects.equals(roomId,"null") || sessionId ==null){
                log.info("RoomId가 null 입니다 .");
                return message;
            }

            log.info("Channel outbound : {}",roomId);
            chatService.sendChannelOutboundSessionMessage(roomId,sessionId);

        }
        return message;
    }



}
