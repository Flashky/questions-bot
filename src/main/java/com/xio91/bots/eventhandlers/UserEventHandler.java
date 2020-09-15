package com.xio91.bots.eventhandlers;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.event.user.PrivateNoticeEvent;
import org.kitteh.irc.client.library.event.user.ServerNoticeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xio91.bots.service.BotService;

import lombok.NonNull;
import net.engio.mbassy.listener.Handler;

@Component
public class UserEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(UserEventHandler.class);
	
	private static final String AUTH_FAILED = "Login authentication failed";
	
	@Autowired 
	private BotService botService;
	
	@Handler
	public void onServerNotice(ServerNoticeEvent event) {
		LOG.warn("Server notice: "+ event.getMessage());
		
		if(AUTH_FAILED.equals(event.getMessage())) {
			reconnect(event.getClient());
		}
		
	}
	
	private void reconnect(@NonNull Client client) {
		client.shutdown();
		LOG.info("Reconnecting client...");
		//botService.connect(oauthToken);
		
	}

	@Handler
	public void onPrivateServerNotice(PrivateNoticeEvent event) {
		LOG.info("Private server notice: " + event.getMessage());
	}
	
}
