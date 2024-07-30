package com.example.livealone.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatDto {

    private String type;
    private String messenger;
    private String message;
}
