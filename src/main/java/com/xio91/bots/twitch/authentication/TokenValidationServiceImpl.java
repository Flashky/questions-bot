package com.xio91.bots.twitch.authentication;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

import com.xio91.bots.twitch.authentication.handlers.OnTokenValidateFailureHandler;

public class TokenValidationServiceImpl implements TokenValidationService {

	private RestTemplate restTemplate;
	private OnTokenValidateFailureHandler failureHandler = null;
	

	public TokenValidationServiceImpl() {
		restTemplate = new RestTemplate();
	}
	
	public void setFailureHandler(OnTokenValidateFailureHandler failureHandler) {
		this.failureHandler = failureHandler;
	}

	
	@Override
	public void validate(OAuth2AuthorizedClient authorizedClient) {
		
		try {
			String accessToken = authorizedClient.getAccessToken().getTokenValue();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "OAuth " + accessToken);
			
			HttpEntity<String> entity = new HttpEntity<>(headers);
			
			//LOG.info("Validating access token...");
		
			restTemplate.exchange("https://id.twitch.tv/oauth2/validate", HttpMethod.GET, entity, String.class);
			
		} catch(Unauthorized ex) {
			
			if(failureHandler != null) {
				failureHandler.handleFailure(authorizedClient, null);
			}
		}
	
	}
	
	
	
	

	
}
