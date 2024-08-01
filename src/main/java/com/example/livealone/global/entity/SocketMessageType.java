package com.example.livealone.global.entity;

public enum SocketMessageType {
    // BACKEND
    REQUEST_AUTH,
    REQUEST_CHAT_INIT,
    REQUEST_REFRESH,
    CHAT_MESSAGE,
    FAILED,

    ERROR,
    BROADCAST,

    // FRONTEND
    RESPONSE_AUTH,
    RESPONSE_CHAT_INIT,
    RESPONSE_REFRESH,
    INVALID_TOKEN,
    ANONYMOUS_USER,
}
