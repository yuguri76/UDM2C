package com.example.livealone.chat.repository;

import com.example.livealone.chat.entity.ChatSessionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatSessionLogRepository extends MongoRepository<ChatSessionLog,String> {
}
