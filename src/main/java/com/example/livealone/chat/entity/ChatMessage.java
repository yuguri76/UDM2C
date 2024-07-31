package com.example.livealone.chat.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chatMessages")
@NoArgsConstructor
@Getter
public class ChatMessage {
    @Id
    private String id;
    private String nickname;
    private String message;
    private LocalDateTime messageTime;

    public ChatMessage(String nickname, String message){
        this.nickname = nickname;
        this.message = message;
        this.messageTime = LocalDateTime.now();
    }
}
