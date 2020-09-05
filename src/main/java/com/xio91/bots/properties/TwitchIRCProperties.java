package com.xio91.bots.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "twitch.irc")
@Data
public class TwitchIRCProperties {

	public static final String DEFAULT_IRC_HOST = "irc.chat.twitch.tv";
	
	private String host = DEFAULT_IRC_HOST;
	private String nick;
	private String channel;
	
}
