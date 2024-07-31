package com.example.livealone.chat.repository;

import com.example.livealone.chat.entity.ChatErrorLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatErrorLogRepository extends MongoRepository<ChatErrorLog,String> {
}
