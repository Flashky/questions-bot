package com.xio91.bots.twitch.authentication.handlers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.xio91.bots.twitch.model.ErrorResponse;

public interface OnErrorResponseHandler {

	void handleFailure(OAuth2AuthorizedClient authorizedClient, ErrorResponse twitchErrorResponse);
	
}
