package com.xio91.bots.tasks;

import java.util.concurrent.atomic.AtomicInteger;

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
public class SessionValidationTask {

	private static final Logger LOG = LoggerFactory.getLogger(SessionValidationTask.class);
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private int invalidatedClients;
    private int expiredSessions;
    
    /**
     * Checks all sessions by schedule.
     * <p>All principal tokens are validated against the Twitch validate endpoint.</p>
     * <p>If the token is not valid, all the sessions from the user owning that token will be invalidated.</p>
     * @see <a href="https://freeformatter.com/cron-expression-generator-quartz.html">Cron expression generator</a>
     */
    @Scheduled(cron = "0 0 * ? * *")
	public void checkSessions() {
		
    	LOG.info("Checking alive sessions...");
    	
    	invalidatedClients = 0;
    	expiredSessions = 0;
    	
    	sessionRegistry.getAllPrincipals()
    					.stream()
        				.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty()) // Filter out users with no sessions
        				.forEach(principal -> validateSession(principal));
    	
    	logResult();
    	
	}

	private void logResult() {
		if(expiredSessions == 0) {
    		LOG.info("No sessions have been expired.");
    	} else {
    		LOG.info("Expired " + expiredSessions + " session/s from "+invalidatedClients+ " client/s.");
    	}
	}

	private void validateSession(Object principal) {
		
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) principal;
		
		// Retrive authorized client which is the one that has the tokens to validate
		OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("twitch", oauth2User.getName());
		
		if(!isValidSession(authorizedClient)) {
			
			AtomicInteger counter = new AtomicInteger(0);
			
			sessionRegistry.getAllSessions(principal, false)
							.stream()
							.peek(u -> counter.getAndIncrement())
							.forEach(SessionInformation::expireNow);
			
			invalidatedClients++;
			expiredSessions += counter.get();
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
