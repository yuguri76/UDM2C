package com.example.livealone.chat.handler;

import com.example.livealone.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatShutdownHandler implements ApplicationListener<ContextClosedEvent> {

    private final ChatService chatService;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        chatService.flush();

    }
}
