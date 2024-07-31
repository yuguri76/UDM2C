package com.example.livealone.chat.repository;

import com.example.livealone.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
}
