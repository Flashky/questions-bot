package com.xio91.bots.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xio91.bots.eventhandlers.ChannelEventHandler;
import com.xio91.bots.eventhandlers.ClientEventHandler;
import com.xio91.bots.eventhandlers.ConnectionEventHandler;
import com.xio91.bots.eventhandlers.TwitchEventHandler;
import com.xio91.bots.eventhandlers.UserEventHandler;
import com.xio91.bots.properties.TwitchIRCProperties;

@Component
public class KiclBotService implements BotService {

	private static final Logger LOG = LoggerFactory.getLogger(KiclBotService.class);
	
	private final static String OAUTH_PREFIX = "oauth:";
	private final static String CHANNEL_PREFIX = "#";
	
	@Autowired
	private TwitchIRCProperties ircProperties;

	private Client client;
	
	@Override
	public void connect(String oauthToken, String nick) {
		
		// Connection configuration
		Builder clientBuilder =  Client.builder()
					.server()
					.host(ircProperties.getHost())
					.password(OAUTH_PREFIX + oauthToken)
				.then()
					.nick(nick);
		 
		// Debug chat input/output
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		clientBuilder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
		clientBuilder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
		clientBuilder.listeners().exception(Throwable::printStackTrace);
		
		
		// Build the client and add twitch support
		client = clientBuilder.build();
		TwitchSupport.addSupport(client);

		
		// Event handlers
		client.getEventManager().registerEventListener(new ConnectionEventHandler());
		client.getEventManager().registerEventListener(new ClientEventHandler());
		client.getEventManager().registerEventListener(new UserEventHandler());
		client.getEventManager().registerEventListener(new TwitchEventHandler());
		client.getEventManager().registerEventListener(new ChannelEventHandler());
		
		
		// Connect and join to channel
		client.connect();
		client.addChannel(CHANNEL_PREFIX + ircProperties.getChannel());
	}

	@Override
	public void disconnect() {
		
		if(client != null) {
			LOG.info("Disconnecting bot");
			client.shutdown();
			LOG.info("Bot has been disconnected");
			
		}

	}

}
