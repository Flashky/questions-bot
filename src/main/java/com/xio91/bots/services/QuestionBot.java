package com.xio91.bots.services;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.kitteh.irc.client.library.event.connection.ClientConnectionClosedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEstablishedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionFailedEvent;
import org.kitteh.irc.client.library.feature.twitch.TwitchSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xio91.bots.eventhandlers.ChannelEventHandler;
import com.xio91.bots.eventhandlers.ClientEventHandler;
import com.xio91.bots.eventhandlers.TwitchEventHandler;
import com.xio91.bots.eventhandlers.UserEventHandler;
import com.xio91.bots.properties.TwitchIRCProperties;

import net.engio.mbassy.listener.Handler;

@Component
public class QuestionBot implements IrcClient {

	private static final Logger LOG = LoggerFactory.getLogger(QuestionBot.class);
	
	private final static String OAUTH_PREFIX = "oauth:";
	private final static String CHANNEL_PREFIX = "#";
	
	@Autowired
	private TwitchIRCProperties ircProperties;

	private Client client;
	
	private boolean connected = false;
	
	@Override
	public void connect(String oauthToken, String nick) {
		
		
		if(connected) {
			// Avoid multiple client connections
			LOG.warn("Bot is already connected!");
			return;
		}
		
		// Connection configuration
		Builder clientBuilder =  Client.builder()
					.server()
					.host(ircProperties.getHost())
					.password(OAUTH_PREFIX + oauthToken)
				.then()
					.nick(nick);
		 
		// Debug chat input/output
		/*
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		clientBuilder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
		clientBuilder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
		clientBuilder.listeners().exception(Throwable::printStackTrace);
		*/
		
		// Build the client and add twitch support
		client = clientBuilder.build();
		TwitchSupport.addSupport(client);

		
		// Event handlers
		client.getEventManager().registerEventListener(new ClientEventHandler());
		client.getEventManager().registerEventListener(new UserEventHandler());
		client.getEventManager().registerEventListener(new TwitchEventHandler());
		client.getEventManager().registerEventListener(new ChannelEventHandler());
		client.getEventManager().registerEventListener(this);
		
		// Connect and join to channel
		client.connect();
		join(ircProperties.getChannel());
		
	}

	@Override
	public void disconnect() {
		
		if(client != null) {
			LOG.info("Disconnecting bot");
			client.shutdown();
			LOG.info("Bot has been disconnected");
			
		}

	}


	@Override
	public void join(String channel) {
		
		if(client != null) {
			client.addChannel(CHANNEL_PREFIX + channel);
		}
		
	}

	@Override
	public void part(String channel) {

		if(client != null) {
			client.removeChannel(CHANNEL_PREFIX + channel);
		}
		
	}
	
	@Handler 
	public void onConnectionEstablished(ClientConnectionEstablishedEvent event) {
		
		LOG.info("Twitch IRC connection established.");
		this.connected = true;
		
	}
	
	@Handler
	public void onConnectionClosed(ClientConnectionClosedEvent event) {
		
		LOG.warn("Twitch IRC connection closed. Last message: "+ event.getLastMessage().orElse("No message"));
		this.connected = false;
		
	}
	
	@Handler
	public void onConnectionFailed(ClientConnectionFailedEvent event) {
		
		LOG.warn("Twitch IRC connection has failed.");
		this.connected = false;
		
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	
	
}
