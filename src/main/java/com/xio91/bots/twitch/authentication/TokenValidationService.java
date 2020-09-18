package com.xio91.bots.twitch.authentication;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface TokenValidationService {


	/**
	 * Validates the authorized client token if possible.
	 * @param authorizedClient The authorized client to refresh the token.
	 */
	void validate(OAuth2AuthorizedClient authorizedClient);
}
