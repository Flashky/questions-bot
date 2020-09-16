package com.xio91.bots.controllers.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.xio91.bots.service.AuthenticationService;
import com.xio91.bots.service.IrcClient;


@RestController
public class BotController {

	private static final Logger LOG = LoggerFactory.getLogger(BotController.class);
	
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private IrcClient botService;
    

	

	@GetMapping("/irc/clients")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void createIrcClient() {
		
		OAuth2AuthorizedClient authorizedClient = authenticationService.validateAccessToken();
		
		// Validate and connect
		//tokenService.validateToken(accessToken);
		botService.connect(authorizedClient.getAccessToken().getTokenValue(), authorizedClient.getPrincipalName());

		botService.connect(authorizedClient.getAccessToken().getTokenValue(), authorizedClient.getPrincipalName());
	}

	
}
