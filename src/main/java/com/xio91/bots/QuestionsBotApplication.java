package com.xio91.bots;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.xio91.bots.properties.TwitchIRCProperties;
import com.xio91.bots.service.BotService;

@SpringBootApplication
@EnableFeignClients
public class QuestionsBotApplication implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(QuestionsBotApplication.class);
	
	private final static String OAUTH_PREFIX = "oauth:";
	private final static String CHANNEL_PREFIX = "#";
	
	@Autowired
	private TwitchIRCProperties ircProperties;
	
	@Autowired
	private BotService botService;
	
	public static void main(String[] args) {
		SpringApplication.run(QuestionsBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		
		
		//String oauthToken = tokenService.refreshToken(args[1]);
		//botService.connect(oauthToken + "a");
		/*
		while(true) {
			tokenService.validateToken("aaa");
			tokenService.revokeToken(newToken);
			tokenService.validateToken(newToken);
			LOG.info("Sleeping.");
			Thread.sleep(1800000);
			LOG.info("Wake up");
		}
		*/
		


		
	}
}
