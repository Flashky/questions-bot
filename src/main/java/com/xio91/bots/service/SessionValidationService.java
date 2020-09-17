package com.xio91.bots.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

import com.xio91.bots.service.dto.TokenResponse;

@Service
public class SessionValidationService {

	private static final Logger LOG = LoggerFactory.getLogger(SessionValidationService.class);
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    @Autowired
    private AuthenticationService authService;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Autowired
    private RestTemplate restTemplate;
    
    //@Scheduled(cron = "0 0 0/1 * * ?")
    @Scheduled(fixedRate = 10000)
	public void checkSession() {
		
    	LOG.info("Checking alive sessions...");
    	
    	sessionRegistry.getAllPrincipals()
    					.stream()
        				.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
        				.forEach(principal -> validateSession(principal));
       
        
	}

	private void validateSession(Object principal) {
		
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
		
		// Generate Authentication object
		OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), "twitch");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		validateSession(authentication);
		
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	private void validateSession(OAuth2AuthenticationToken oauth2Token) {
		
		OAuth2AuthorizedClient authorizedClient = getAuthorizedClient(oauth2Token);

		if(tokenHasExpired(authorizedClient.getAccessToken())) {
			LOG.info(authorizedClient.getPrincipalName() + " token is not longer valid.");
			refreshToken(authorizedClient, oauth2Token);
		}
	
	}
	
	private void refreshToken(OAuth2AuthorizedClient authorizedClient, OAuth2AuthenticationToken oauth2Token) {
		
		try {
			ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
			
			// Prepare request headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			// Prepare body request
			MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
			form.add("client_id", clientRegistration.getClientId());
			form.add("client_secret", clientRegistration.getClientSecret());
			form.add("grant_type", "refresh_token");
			form.add("refresh_token", authorizedClient.getRefreshToken().getTokenValue());
			
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(form, headers);
		
		
			ResponseEntity<TokenResponse> refreshTokenResponse = restTemplate.exchange("https://id.twitch.tv/oauth2/token", HttpMethod.POST, request, TokenResponse.class);
			updateSession(authorizedClient, refreshTokenResponse.getBody());
			
		} catch (BadRequest ex) {
			LOG.warn("Couldn't refresh token: " + ex.getMessage());
		}
		
	}

	private boolean tokenHasExpired(OAuth2AccessToken oauth2AccessToken) {
		
		try {
			String accessToken = oauth2AccessToken.getTokenValue();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "OAuth " + accessToken);
			
			HttpEntity<String> entity = new HttpEntity<>(headers);
			
			LOG.info("Validating access token...");
		
			restTemplate.exchange("https://id.twitch.tv/oauth2/validate", HttpMethod.GET, entity, String.class);
			
		} catch (Unauthorized ex) {
			LOG.warn("Access token has expired.");
			return true;
		}
		
		return false;
		
	}

	
	private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
		return authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), 
															authentication.getPrincipal().getName());
	}
	

	/**
	 * @param authorizedClient
	 * @param clientRegistration
	 * @param refreshTokenResponse
	 * @return
	 */
	private void updateSession(OAuth2AuthorizedClient authorizedClient, TokenResponse refreshTokenResponse) {
		
		// Retrieve client registration
		ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
		
		OAuth2AccessToken newAccessToken = new OAuth2AccessToken(TokenType.BEARER, 
													refreshTokenResponse.getAccessToken(), 
													Instant.now(), 
													Instant.now().plusSeconds(refreshTokenResponse.getExpirationSeconds()), 
													refreshTokenResponse.getScopes());
		
		OAuth2AuthorizedClient newAuthorizedClient = new OAuth2AuthorizedClient(clientRegistration, 
																		authorizedClient.getPrincipalName(),
																		newAccessToken, 
																		authorizedClient.getRefreshToken());
		
		authorizedClientService.saveAuthorizedClient(newAuthorizedClient, SecurityContextHolder.getContext().getAuthentication());
	}

}
