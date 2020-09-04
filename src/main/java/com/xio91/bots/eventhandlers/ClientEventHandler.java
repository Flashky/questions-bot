package com.xio91.bots.eventhandlers;

import org.kitteh.irc.client.library.event.client.ClientNegotiationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.engio.mbassy.listener.Handler;

public class ClientEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ClientEventHandler.class);
	
	@Handler
	public void onClientNegotiationComplete(ClientNegotiationCompleteEvent event) {
	
		// Client has sucessfully logged in on Twitch IRC
		
		LOG.info("Client negotiation completed. Bot has logged in on '"+event.getServer().getName()+"'");

		// TODO Añadir unión a canal? no sé si este handler debería de conocer esto.
	}
}
