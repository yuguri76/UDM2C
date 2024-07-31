package com.example.livealone.global.dto;

import com.example.livealone.global.entity.SocketMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/**
 * 프론트와 소켓 통신을 할 때 기준
 */
public class SocketMessageDto {

    private SocketMessageType type;
    private String messenger;
    private String message;
}
