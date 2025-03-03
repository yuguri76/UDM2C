package com.example.livealone.global.entity;

public enum SocketMessageType {
    // BACKEND
    REQUEST_AUTH,
    REQUEST_CHAT_INIT,
    REQUEST_REFRESH,
    REQUEST_VIEWERCOUNT,
    CHAT_MESSAGE,
    FAILED,
    ERROR,
    BROADCAST,
    ALERT_BROADCAST_START,
    ALERT_ALMOST_SOLD_OUT,
    ALERT_SOLD_OUT,
    THROW,
    // FRONTEND
    RESPONSE_AUTH,
    RESPONSE_CHAT_INIT,
    RESPONSE_REFRESH,
    RESPONSE_VIEWERCOUNT,
    INVALID_TOKEN,
    ANONYMOUS_USER,
}
