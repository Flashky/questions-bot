package com.xio91.bots.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestTemplate;

@Service
public class SessionValidationService {

	private static final Logger LOG = LoggerFactory.getLogger(SessionValidationService.class);
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Autowired
    private RestTemplate restTemplate;
    
    // Check: https://freeformatter.com/cron-expression-generator-quartz.html
    //@Scheduled(cron = "0 33 * * * ?")
    @Scheduled(cron = "0 */2 * ? * *")
    //@Scheduled(fixedRate = 10000)
	public void checkSessions() {
		
    	LOG.info("Checking alive sessions...");
    	
    	sessionRegistry.getAllPrincipals()
    					.stream()
        				.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty()) // Filter out users with no sessions
        				.forEach(principal -> validateSession(principal));
    	
	}

	private void validateSession(Object principal) {
		
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
		
		// Retrive authorized client which is the one that has the tokens to validate
		OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("twitch", oauth2User.getName());
		
		if(!isValidSession(authorizedClient)) {
			sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);
		}
		
		
	}

	private boolean isValidSession(OAuth2AuthorizedClient authorizedClient) {
		
		if(authorizedClient == null) {
			return false;
		} else {
			return isValidToken(authorizedClient);
		}
	}

	private boolean isValidToken(OAuth2AuthorizedClient authorizedClient) {
		
		boolean valid = true;
		try {
			
			String accessToken = authorizedClient.getAccessToken().getTokenValue();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "OAuth " + accessToken);
			
			HttpEntity<String> entity = new HttpEntity<>(headers);
			
			restTemplate.exchange("https://id.twitch.tv/oauth2/validate", HttpMethod.GET, entity, String.class);
			
			LOG.debug("Token is still valid for user: "+authorizedClient.getPrincipalName());
			
		} catch(Unauthorized ex) {
			LOG.debug("Token is invalid for user: "+authorizedClient.getPrincipalName());
			valid = false;
		} catch(Exception ex) {
			LOG.debug("Could not validate token for user: "+authorizedClient.getPrincipalName());
		}
		
		return valid;
	}

}
