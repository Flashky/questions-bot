package com.xio91.bots.controllers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xio91.bots.properties.TwitchIRCProperties;
import com.xio91.bots.services.IrcClient;

@RestController
public class BotController {

	private static final Logger LOG = LoggerFactory.getLogger(BotController.class);
	
	@Autowired
	private IrcClient questionBot;
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
    @Autowired 
    private TwitchIRCProperties twitchIrcProperties;
    
	@PostMapping("/connect")
	public void connect(Authentication authentication) {
	
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
		
		if(isAllowedToConnect((oauth2Token))) { 
			connectBot(oauth2Token);
		}
		
	}
	
	/**
	 * Determines if the authenticated user is allowed to connect to IRC or not.
	 * <p>Connection conditions (all must be met):</p>
	 * <ul>
	 * 	<li>Bot is not already connected to IRC.</li>
	 * 	<li>Principal name matches bot name, which means the authenticated person is the bot itself.</li>
	 * </ul>
	 * @param authentication
	 * @return true if the current user is allowed to connect to IRC. false in other case.
	 */
	private boolean isAllowedToConnect(OAuth2AuthenticationToken authentication) {

		String principalName = authentication.getPrincipal().getName();
		
		boolean isConnected = questionBot.isConnected();
		boolean principalIsBot = twitchIrcProperties.getNick().equals(principalName);
		boolean isAllowed = (!isConnected) && (principalIsBot);
		
		LOG.debug("Bot is already connected = " + isConnected);
		LOG.debug("Authenticated principal is bot = " + principalIsBot);
		LOG.debug("Allowed to connect = " + isAllowed);
		
		return isAllowed;
	}

	private void connectBot(OAuth2AuthenticationToken authentication) {
		
		// Obtain authorized client to extract the oauth token
		OAuth2AuthorizedClient authorizedClient = authorizedClientService
														.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), 
																				authentication.getPrincipal().getName());
		
		
		// Finally, connect the bot
		questionBot.connect(authorizedClient.getAccessToken().getTokenValue(), authorizedClient.getPrincipalName());
	}
}
