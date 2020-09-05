package com.xio91.bots.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {

	@Autowired
	private OAuth2ClientProperties oauth2ClientProperties;
	
	@GetMapping("/callback")
	public void callback() {
		
		System.out.println("Callback!!");
		System.out.println(oauth2ClientProperties.getProvider().get("twitch").getTokenUri());
		
		// TODO:
		// 1. Solicitar el token 
		// 2. Iniciar el bot
		
		// Avanzado: Redireccionar a otra URL (authenticated restricted) en el que se pueda configurar y arrancar al bot.
	}

}
