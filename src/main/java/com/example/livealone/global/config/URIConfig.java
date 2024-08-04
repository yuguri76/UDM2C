package com.example.livealone.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class URIConfig {

    @Value("${uri.back-server}")
    private String serverHost;

    @Value("${uri.front-server}")
    private String frontServerHost;


    @Value("${uri.database}")
    private String databaseHost;

    @Value("${uri.kafka}")
    private String kafkaHost;

}
