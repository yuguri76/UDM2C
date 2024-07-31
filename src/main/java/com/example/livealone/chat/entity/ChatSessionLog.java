package com.example.livealone.chat.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sessionLogs")
public class ChatSessionLog {
    @Id
    private String id;
    private String nickname;
    private String sessionId;
    private LocalDateTime sessionTime;

    public ChatSessionLog(String nickname, String sessionId) {
        this.nickname = nickname;
        this.sessionId = sessionId;
        this.sessionTime = LocalDateTime.now();
    }
}
