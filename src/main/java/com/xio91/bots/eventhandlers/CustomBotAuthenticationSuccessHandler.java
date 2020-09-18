package com.xio91.bots.eventhandlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.xio91.bots.properties.TwitchIRCProperties;
import com.xio91.bots.services.QuestionBot;


/**
 * Handles the authorization sucess.
 * <p>
 * This custom handler automatically performs the bot connection to Twitch after the authorization has been granted.
 * </p>
 * <p>This action won't be performed if the bot is already connected to Twitch or the authenticated user is not the bot.</p>
 * @author bruizv
 *
 */
@Component
public class CustomBotAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {

	private static final Logger LOG = LoggerFactory.getLogger(CustomBotAuthenticationSuccessHandler.class);
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
    @Autowired 
    private TwitchIRCProperties twitchIrcProperties;
    
	@Autowired
	private QuestionBot questionBot;
    
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
		
		LOG.info(oauth2Token.getPrincipal().getName() + " has authenticated.");
		
		if(isAllowedToConnect((oauth2Token))) { 
			connectBot(oauth2Token);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
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
