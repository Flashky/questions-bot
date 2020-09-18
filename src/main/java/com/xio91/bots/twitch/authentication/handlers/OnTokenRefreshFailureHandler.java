package com.xio91.bots.twitch.authentication.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.ResponseErrorHandler;

public class OnTokenRefreshFailureHandler implements ResponseErrorHandler{

	private static final Logger LOG = LoggerFactory.getLogger(OnTokenRefreshFailureHandler.class);
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		
		// Only handle the unauthorized response which means the refresh token has expired or revoked.
		return HttpStatus.UNAUTHORIZED.equals(response.getStatusCode());
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		
		OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		LOG.warn("Refresh token is no longer valid for user: " + authentication.getName());
		
		// TODO expire sessions ¿how to do it?
		
	}

}
