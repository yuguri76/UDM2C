package com.example.livealone.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.ChannelInterceptor;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

}
