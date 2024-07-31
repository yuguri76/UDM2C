package com.example.livealone.chat.repository;

import com.example.livealone.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    ArrayList<ChatMessage> findTop30ByOrderByIdDesc();
}
