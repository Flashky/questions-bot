package com.xio91.bots.eventhandlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.xio91.bots.service.QuestionBot;


/**
 * Handles the authorization sucess.
 * <p>
 * This custom handler automatically performs the bot connection to Twitch after the authorization has been granted.
 * </p>
 * <p>If the bot is already connected to Twitch, no action will be performed.
 * @author bruizv
 *
 */
@Component
public class CustomBotAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {

	@Autowired
	private QuestionBot questionBot;
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		if(!questionBot.isConnected()) {
			connectBot(authentication);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private void connectBot(Authentication authentication) {
		
		// Obtain authorized client to extract the oauth token
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient authorizedClient = authorizedClientService
														.loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), 
																				oauth2Token.getPrincipal().getName());
		
		
		// Finally, connect the bot
		questionBot.connect(authorizedClient.getAccessToken().getTokenValue(), authorizedClient.getPrincipalName());
	}

}
