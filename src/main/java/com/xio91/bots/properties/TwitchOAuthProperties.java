package com.xio91.bots.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "twitch.oauth")
@Data
public class TwitchOAuthProperties {

	private String clientId;
	private String clientSecret;
	
}
