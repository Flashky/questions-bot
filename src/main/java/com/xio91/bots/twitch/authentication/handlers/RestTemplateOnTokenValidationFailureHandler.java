package com.xio91.bots.twitch.authentication.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.ResponseErrorHandler;

public class RestTemplateOnTokenValidationFailureHandler implements ResponseErrorHandler {

	private static final Logger LOG = LoggerFactory.getLogger(OnTokenRefreshFailureHandler.class);
	
	private static final String TWITCH_REGISTRATION_ID = "twitch";
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		
		// Only handle the unauthorized response which means the access token has expired or revoked.
		return HttpStatus.UNAUTHORIZED.equals(response.getStatusCode());
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		
		OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		LOG.warn("Refresh token is no longer valid for user: " + authentication.getName());
		
		// TODO attempt to refresh
		
		OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(TWITCH_REGISTRATION_ID, authentication.getName());
		
		
	}

}
