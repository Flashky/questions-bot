package com.xio91.bots.eventhandlers;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelNoticeEvent;
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.engio.mbassy.listener.Handler;

/**
 * Handles any channel related events.
 * 
 * @author bruizv
 *
 */
public class ChannelEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ChannelEventHandler.class);
	
	@Handler
	public void onSuccessfulChannelJoin(RequestedChannelJoinCompleteEvent event) {
		
		// Stop loggin server messages
		event.getClient().setInputListener(null);
		event.getClient().setOutputListener(null);
		
		LOG.info("Joined channel: "+event.getChannel().getName());
	}
	
	@Handler
	public void onChannelMessage(ChannelMessageEvent event) {
		
		String message = event.getMessage();

		LOG.info(event.getActor().getNick() +": "+ event.getMessage());
		
		// Sample for handling commands
		if(message.startsWith("!question ")) {
			LOG.info("!question command requested by: " + event.getClient().getNick());
		} else if(message.startsWith("!discord")) {
			LOG.info("!discord command requested by: " + event.getClient().getNick());
		} 
		
		// TODO Crear una clase abstracta que siga el patrón abstract method:
		 
		//Métodos abstractos:
		// - getCommand(): devuelve el comando al que responder.
		// - executeCommand(MessageEvent): ejecuta el comando en si mismo.
		
		// Esto ahorraría meter en todos los comandos la lógica de "if(message.startsWith(X)) { do something }"
		// En la clase abstracta if(message.startsWith(getCommand())) { executeCommand(event) }
		
		// Hay que tener también en cuenta los parámetros. Algunos comandos pueden tener parámetros y otros no.
	}	
	
	@Handler
	public void onChannelNotice(ChannelNoticeEvent event) {
		
		LOG.info(event.getMessage());
		
	}


	
}
