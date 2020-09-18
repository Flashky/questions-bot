package com.xio91.bots.twitch.authentication.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

import com.xio91.bots.twitch.model.ErrorResponse;

@Component
public class OnTokenValidateFailureHandler implements OnErrorResponseHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OnTokenRefreshFailureHandler.class);
    
	@Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
	@Override
	public void handleFailure(OAuth2AuthorizedClient authorizedClient, ErrorResponse twitchErrorResponse) {
	
		// Refresh token, only if user is the bot
		LOG.warn("Access token is no longer valid for user: " + authorizedClient.getPrincipalName());
		authorizedClientService.removeAuthorizedClient(authorizedClient.getClientRegistration().getClientId(), authorizedClient.getPrincipalName());
		
		// TODO remove sessions from authorized client
		
	}

}
