package com.example.livealone.chat.service;

import com.example.livealone.global.dto.SocketMessageDto;
import com.example.livealone.chat.dto.ChatInitDto;
import com.example.livealone.chat.entity.ChatErrorLog;
import com.example.livealone.chat.entity.ChatMessage;
import com.example.livealone.chat.entity.ChatSessionLog;
import com.example.livealone.global.handler.WebSocketHandler;
import com.example.livealone.chat.repository.ChatErrorLogRepository;
import com.example.livealone.chat.repository.ChatMessageRepository;
import com.example.livealone.chat.repository.ChatSessionLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.example.livealone.global.entity.SocketMessageType.RESPONSE_CHAT_INIT;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatErrorLogRepository chatErrorLogRepository;
    private final ChatSessionLogRepository chatSessionLogRepository;
    private final ObjectMapper objectMapper;

    private static final int batchSize = 100;
    private final ConcurrentLinkedQueue<ChatMessage> messageBuffer = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ChatErrorLog> errorLogsBuffer = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ChatSessionLog> sessionLogsBuffer = new ConcurrentLinkedQueue<>();

    /**
     * 현재 연결되어있는 세션에 모두 메시지 전송
     */
    public void write(String message) {
        saveMessage(message);

        ConcurrentHashMap<String, WebSocketSession> clients = WebSocketHandler.getClients();
        TextMessage text = new TextMessage(message);

        clients.forEach((key, value) -> {
            try {
                value.sendMessage(text);
            } catch (IOException e) {
                ChatErrorLog chatErrorLog = new ChatErrorLog((e.getMessage()));
                errorLogsBuffer.add(chatErrorLog);
                if(errorLogsBuffer.size()>batchSize){
                    saveErrorLogs();
                }
            }
        });
    }

    public void writeInitMessage(WebSocketSession session) {

        try {
            List<ChatMessage> chatList = chatMessageRepository.findTop30ByOrderByIdDesc();

            List<ChatInitDto> initData = new ArrayList<>();
            for (ChatMessage chat : chatList) {
                String initNickname = chat.getNickname();
                String initText = chat.getMessage();
                ChatInitDto init = new ChatInitDto(initNickname, initText);
                initData.add(init);
            }
            Collections.reverse(initData);
            String messageJSON = objectMapper.writeValueAsString(initData);
            SocketMessageDto socketMessageDto = new SocketMessageDto(RESPONSE_CHAT_INIT, "back-server", messageJSON);

            String result = objectMapper.writeValueAsString(socketMessageDto);
            TextMessage text = new TextMessage(result);

            session.sendMessage(text);

        } catch (IOException e) {
            log.info(e.getMessage());
            addErrorLogs(e.getMessage());
        }
    }

    private void saveMessage(String message) {

        try {
            SocketMessageDto socketMessageDto = objectMapper.readValue(message, SocketMessageDto.class);

            switch (socketMessageDto.getType()) {
                case REQUEST_AUTH -> {
                    ChatSessionLog chatSessionLog = new ChatSessionLog(socketMessageDto.getMessenger(), socketMessageDto.getMessage());

                    sessionLogsBuffer.add(chatSessionLog);
                    if (sessionLogsBuffer.size() > batchSize) {
                        saveSessionLogs();
                    }
                }
                case CHAT_MESSAGE -> {
                    //ChatMessage chatMessage = new ChatMessage(socketMessageDto.getMessenger(), socketMessageDto.getMessage());
                    chatMessageRepository.save(new ChatMessage(socketMessageDto.getMessenger(), socketMessageDto.getMessage()));
                    //messageBuffer.add(chatMessage);
                    if (messageBuffer.size() > batchSize) {
                        saveChatMessages();
                    }
                }
                case FAILED -> {
                    ChatErrorLog chatErrorLog = new ChatErrorLog(socketMessageDto.getMessage());
                    errorLogsBuffer.add(chatErrorLog);
                    if (errorLogsBuffer.size() > batchSize) {
                        saveErrorLogs();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            addErrorLogs(e.getMessage());
        }
    }

    private void addErrorLogs(String message){
        ChatErrorLog chatErrorLog = new ChatErrorLog((message));
        errorLogsBuffer.add(chatErrorLog);
        if(errorLogsBuffer.size()>batchSize){
            saveErrorLogs();
        }
    }

    private synchronized void saveChatMessages() {
        if (!messageBuffer.isEmpty()) {
            chatMessageRepository.saveAll(new LinkedList<>(messageBuffer));
            messageBuffer.clear();
        }
    }

    private synchronized void saveErrorLogs() {
        if (!errorLogsBuffer.isEmpty()) {
            chatErrorLogRepository.saveAll(new LinkedList<>(errorLogsBuffer));
            errorLogsBuffer.clear();
        }
    }

    private synchronized void saveSessionLogs() {
        if (!sessionLogsBuffer.isEmpty()) {
            chatSessionLogRepository.saveAll(new LinkedList<>(sessionLogsBuffer));
            sessionLogsBuffer.clear();
        }
    }


    public void flush() {
        log.info("서버 종료 전 버퍼에 있는 데이터 저장");
        saveChatMessages();
        saveErrorLogs();
        saveSessionLogs();
    }
}
