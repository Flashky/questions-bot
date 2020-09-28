package com.xio91.bots.eventhandlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.channel.ChannelNoticeEvent;
import org.kitteh.irc.client.library.event.channel.RequestedChannelJoinCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xio91.bots.rest.questions.model.Question;
import com.xio91.bots.services.QuestionsService;

import net.engio.mbassy.listener.Handler;

/**
 * Handles any channel related events.
 * 
 * @author bruizv
 *
 */
@Component
public class ChannelEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ChannelEventHandler.class);

	@Autowired
	private QuestionsService questionsService;
	
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
			createQuestion(event);
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

	/**
	 * @param event
	 */
	private void createQuestion(ChannelMessageEvent event) {
		LOG.info("!question command requested by: " + event.getClient().getNick());

		String nick = event.getClient().getNick();
		
		String commandRegex = "(?<=!question )(.*$)";
		Pattern pattern = Pattern.compile(commandRegex);
		
		Matcher m = pattern.matcher(event.getMessage());
	      
	      if (m.find( )) {
	    	  
	    	  String questionText = m.group(0);
	    	  LOG.info("Question: '"+ questionText + "' by "+ nick);
	    	  
	    	  Question question = new Question(questionText, nick);
	    	  questionsService.createQuestion(question);
	      }
		
	}


	@Handler
	public void onChannelNotice(ChannelNoticeEvent event) {
		
		LOG.info(event.getMessage());
		
	}


	
}
