package com.xio91.bots.eventhandlers;

import org.kitteh.irc.client.library.event.user.PrivateNoticeEvent;
import org.kitteh.irc.client.library.event.user.ServerNoticeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.engio.mbassy.listener.Handler;

public class UserEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(UserEventHandler.class);
	
	@Handler
	public void onServerNotice(ServerNoticeEvent event) {
		LOG.warn("Server notice: "+ event.getMessage());
	}
	
	@Handler
	public void onPrivateServerNotice(PrivateNoticeEvent event) {
		LOG.info("Private server notice: " + event.getMessage());
	}
	
}
