package com.xio91.bots.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.xio91.bots.twitch.model.TokenResponse;

@Component
public class AuthenticationService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    @Autowired
    private WebClient webClient;
    
	public OAuth2AuthorizedClient validateAccessToken() {
		
		// Retrieve authorized client current information
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthorizedClient authorizedClient = authorizedClientService
													.loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), 
															oauth2Token.getPrincipal().getName());
		
		// Validate authorized client's current access token
		ClientResponse response = validateAccessToken(authorizedClient);
		
		
		// Refresh token if needed
		if(HttpStatus.UNAUTHORIZED.equals(response.statusCode())) {
			
			LOG.info("Twitch access token has expired. Refreshing...");
			TokenResponse refreshTokenResponse = refreshAccessToken(authorizedClient);
			
			OAuth2AuthorizedClient updatedAuthorizedClient = updateAuthorizedClient(authorizedClient, refreshTokenResponse);

			authorizedClient = updatedAuthorizedClient;
			authorizedClientService.saveAuthorizedClient(authorizedClient, oauth2Token);
			
		}
		
		return authorizedClient;
	}

	/**
	 * Validates the authorized client's access token against he validation endpoint.
	 * @param authorizedClient
	 * @return a ClientResponse with the result of validating the token
	 */
	private ClientResponse validateAccessToken(OAuth2AuthorizedClient authorizedClient) {
		
		String accessToken = authorizedClient.getAccessToken().getTokenValue();
		LOG.info("Access token: "+ accessToken);
		
		// Token validation to trigger refresh
		LOG.info("Validating token...");	
		return webClient.get().uri("https://id.twitch.tv/oauth2/validate")
						.header(HttpHeaders.AUTHORIZATION, "OAuth " + accessToken)
						.exchange()
						.block();
		
	}
	
	/**
	 * @param authorizedClient
	 * @param clientRegistration
	 * @return
	 */
	private TokenResponse refreshAccessToken(OAuth2AuthorizedClient authorizedClient) {
		
		// Retrieve client registration
		ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
		
		// Prepare body request
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("client_id", clientRegistration.getClientId());
		form.add("client_secret", clientRegistration.getClientSecret());
		form.add("grant_type", "refresh_token");
		form.add("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
		
		// Refresh the token
		TokenResponse refreshTokenResponse = webClient.post().uri("https://id.twitch.tv/oauth2/token")
		.body(BodyInserters.fromFormData(form))
		.exchange()
		.block()
		.bodyToMono(TokenResponse.class)
		.block();
		
		LOG.info("New access token: " + refreshTokenResponse.getAccessToken());
		return refreshTokenResponse;
	}
	
	/**
	 * @param authorizedClient
	 * @param clientRegistration
	 * @param refreshTokenResponse
	 * @return
	 */
	private OAuth2AuthorizedClient updateAuthorizedClient(OAuth2AuthorizedClient authorizedClient, TokenResponse refreshTokenResponse) {
		
		// Retrieve client registration
		ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
		
		OAuth2AccessToken newAccessToken = new OAuth2AccessToken(TokenType.BEARER, 
													refreshTokenResponse.getAccessToken(), 
													Instant.now(), 
													Instant.now().plusSeconds(refreshTokenResponse.getExpirationSeconds()), 
													refreshTokenResponse.getScopes());
		
		OAuth2AuthorizedClient newClient = new OAuth2AuthorizedClient(clientRegistration, 
																		authorizedClient.getPrincipalName(),
																		newAccessToken, 
																		authorizedClient.getRefreshToken());
		return newClient;
	}


	
}
