package com.xio91.bots.properties.eventhandlers;

import org.kitteh.irc.client.library.event.helper.MessageEvent;

import net.engio.mbassy.listener.Handler;

public class ChannelMessageEventHandler {

	@Handler
	public void onMessageEvent(MessageEvent event) {
		
		System.out.println(event.getMessage());
		
		String message = event.getMessage();
		
		// Sample for handling commands
		if(message.startsWith("!question ")) {
			System.out.println("question command!!");
		} else if(message.startsWith("!discord")) {
			System.out.println("discord command!!");
		} 
		
		// TODO Crear una clase abstracta que siga el patrón abstract method:
		 
		//Métodos abstractos:
		// - getCommand(): devuelve el comando al que responder.
		// - executeCommand(MessageEvent): ejecuta el comando en si mismo.
		
		// Esto ahorraría meter en todos los comandos la lógica de "if(message.startsWith(X)) { do something }"
		// En la clase abstracta if(message.startsWith(getCommand())) { executeCommand(event) }
		
		// Hay que tener también en cuenta los parámetros. Algunos comandos pueden tener parámetros y otros no.
	}
}
