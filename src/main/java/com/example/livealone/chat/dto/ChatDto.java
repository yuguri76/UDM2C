package com.example.livealone.chat.dto;

import com.example.livealone.chat.entity.ChatMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatDto {

    private ChatMessageType type;
    private String messenger;
    private String message;
}
