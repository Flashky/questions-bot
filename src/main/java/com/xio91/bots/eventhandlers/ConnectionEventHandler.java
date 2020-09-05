package com.xio91.bots.eventhandlers;

import org.kitteh.irc.client.library.event.connection.ClientConnectionClosedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionEstablishedEvent;
import org.kitteh.irc.client.library.event.connection.ClientConnectionFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.engio.mbassy.listener.Handler;

public class ConnectionEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ConnectionEventHandler.class);
	
	
	@Handler 
	public void onConnectionEstablished(ClientConnectionEstablishedEvent event) {
		
		LOG.info("Twitch IRC connection established");
	}
	
	@Handler
	public void onConnectionClosed(ClientConnectionClosedEvent event) {
		
		LOG.warn("Twitch IRC connection closed. Last message: "+ event.getLastMessage().orElse("No message"));

		if(event.getCause().isPresent()) {
			LOG.warn("Cause:");
			LOG.warn(event.getCause().get().getMessage());
		}
		
		if(event.willAttemptReconnect()) {
			LOG.info("Reconnecting in: "+event.getReconnectionDelay() + "ms");
		}
		
	}
	
	@Handler
	public void onConnectionFailed(ClientConnectionFailedEvent event) {
		
		LOG.warn("Twitch IRC connection has failed:");

		if(event.getCause().isPresent()) {
			LOG.warn("Cause:");
			LOG.warn(event.getCause().get().getMessage());
		}
		
		if(event.willAttemptReconnect()) {
			LOG.info("Reconnecting in: "+event.getReconnectionDelay() + "ms");
		}
		
	}
	
}
