package com.example.livealone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class LivealoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivealoneApplication.class, args);
	}

}
