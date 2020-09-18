package com.xio91.bots.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xio91.bots.twitch.authentication.TokenValidationService;
import com.xio91.bots.twitch.authentication.TokenValidationServiceImpl;
import com.xio91.bots.twitch.authentication.handlers.OnTokenValidateFailureHandler;

@Configuration
public class TokenHandlersConfig {

	@Bean
	public TokenValidationService tokenValidationService(OnTokenValidateFailureHandler failureHandler) {
		
		TokenValidationServiceImpl tokenValidationService = new TokenValidationServiceImpl();
		tokenValidationService.setFailureHandler(failureHandler);
		
		return tokenValidationService;
	}
	
	
}
