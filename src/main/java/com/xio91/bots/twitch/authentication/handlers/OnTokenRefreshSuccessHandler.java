package com.xio91.bots.twitch.authentication.handlers;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.xio91.bots.twitch.model.TokenResponse;


public interface OnTokenRefreshSuccessHandler {
	
	void handleSucess(OAuth2AuthorizedClient authorizedClient, TokenResponse refreshedToken);
	
}
