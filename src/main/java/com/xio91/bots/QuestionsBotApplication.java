package com.xio91.bots;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xio91.bots.properties.TwitchIRCProperties;
import com.xio91.bots.properties.eventhandlers.ChannelMessageEventHandler;

@SpringBootApplication
public class QuestionsBotApplication implements CommandLineRunner {

	private final static String OAUTH_PREFIX = "oauth:";
	private final static String CHANNEL_PREFIX = "#";
	
	@Autowired
	private TwitchIRCProperties ircProperties;

	
	public static void main(String[] args) {
		SpringApplication.run(QuestionsBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		// Connection configuration
		Builder clientBuilder =  Client.builder()
					.server()
					.host(ircProperties.getHost())
					.password(OAUTH_PREFIX + args[1])
				.then()
					.nick(ircProperties.getNick());
		 
		// Debug chat input/output
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		clientBuilder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
		clientBuilder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
		clientBuilder.listeners().exception(Throwable::printStackTrace);
		 
		// Build the client and add twitch support
		Client client = clientBuilder.build();
		TwitchSupport.addSupport(client);
		client.getEventManager().registerEventListener(new ChannelMessageEventHandler());
		
		// Connect and joint to channel
		client.connect();
		client.addChannel(CHANNEL_PREFIX + ircProperties.getChannel());
		
	}

}
