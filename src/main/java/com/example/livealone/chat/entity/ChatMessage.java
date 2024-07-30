package com.example.livealone.chat.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatMessages")
public class ChatMessage {
    @Id
    private Long id;
    private String user;
    private String message;
    private LocalDateTime messageTime;
}
