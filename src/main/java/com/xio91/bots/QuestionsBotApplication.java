package com.xio91.bots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xio91.bots.properties.TwitchIRCProperties;
import com.xio91.bots.properties.TwitchOAuthProperties;

@SpringBootApplication
public class QuestionsBotApplication implements CommandLineRunner {

	@Autowired
	private TwitchIRCProperties ircProperties;
	
	@Autowired
	private TwitchOAuthProperties oauthProperties;
	
	public static void main(String[] args) {
		SpringApplication.run(QuestionsBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(ircProperties);
		System.out.println(oauthProperties);
	}

}
