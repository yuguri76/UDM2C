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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final ChatService chatService;

    private static final ConcurrentMap<String, List<String>> sessionMap = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        assert headerAccessor != null;
        if (headerAccessor.getCommand() == StompCommand.CONNECT) { // 연결 성공 시
            log.info("CONNECT이벤트 발생");
            String roomId = String.valueOf(headerAccessor.getFirstNativeHeader("RoomId"));

            sessionMap.computeIfAbsent(roomId, k -> new LinkedList<>());
            List<String> sessionListByRoom = sessionMap.get(roomId);

            sessionListByRoom.add(headerAccessor.getSessionId());
            chatService.setViewerCount(roomId,sessionListByRoom.size());
        }
        else if(headerAccessor.getCommand() == StompCommand.DISCONNECT){
            log.info("DISCONNECT이벤트 발생");
            String roomId = String.valueOf(headerAccessor.getFirstNativeHeader("RoomId"));
            if(roomId.isEmpty()){
                log.error("헤더에 roomId가 포함되지 않았습니다.");
                return message;
            }

            log.info("roomId : {}",roomId);
            List<String> sessionListByRoom = sessionMap.get(roomId);
            if(sessionListByRoom ==null){
                log.error("해당 방이 없습니다.");
                return message;
            }
            sessionListByRoom.remove(headerAccessor.getSessionId());
            chatService.setViewerCount(roomId, sessionListByRoom.size());
        }
        return message;
    }



}
