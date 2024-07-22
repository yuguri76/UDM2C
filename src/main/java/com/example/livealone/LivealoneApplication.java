package com.example.livealone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LivealoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivealoneApplication.class, args);
	}

}
