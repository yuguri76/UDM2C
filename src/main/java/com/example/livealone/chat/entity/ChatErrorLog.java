package com.example.livealone.chat.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatErrorLogs")
public class ChatErrorLog {
    @Id
    private String id;
    private String content;
    private LocalDateTime errorTime;

    public ChatErrorLog(String content){
        this.content = content;
        this.errorTime = LocalDateTime.now();
    }
}
