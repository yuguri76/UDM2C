package com.example.livealone.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChatInitDto {
    private String initNickname;
    private String initText;
}
