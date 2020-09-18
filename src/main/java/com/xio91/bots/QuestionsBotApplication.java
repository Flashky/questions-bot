package com.xio91.bots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuestionsBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionsBotApplication.class, args);
	}

}
