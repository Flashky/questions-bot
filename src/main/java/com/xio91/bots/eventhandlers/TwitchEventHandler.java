package com.xio91.bots.eventhandlers;

import org.kitteh.irc.client.library.feature.twitch.event.GlobalUserStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.engio.mbassy.listener.Handler;

public class TwitchEventHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(TwitchEventHandler.class);
	
	@Handler
	public void onGlobalUserState(GlobalUserStateEvent event) {
		
		LOG.info("Global user state: "+event.getSource().getMessage());
	}
	
	
	

}
