package com.xio91.bots.controllers.rest;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

@RestController
public class CallbackController {

	private static final Logger LOG = LoggerFactory.getLogger(CallbackController.class);
	
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
	
   // @Autowired
   // private HttpSessionOAuth2AuthorizedClientRepository repo;
    
    
	@GetMapping("/root")
	public String root() {
 

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		LOG.info("Username (Principal): "+auth.getPrincipal());
		LOG.info("Credentials: "+auth.getCredentials());
		LOG.info("Roles: "+ auth.getAuthorities());
		LOG.info("Details: " + auth.getDetails());
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) securityContext.getAuthentication();
	    		
		OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), 
				oauth2Token.getName());

		LOG.info("Access token: "+ client.getAccessToken().getTokenValue());
		LOG.info("Refresh token: " + client.getRefreshToken().getTokenValue());
		

		return auth.getPrincipal().toString();
	}
	
	@GetMapping("/login-success")
	public String root(Principal principal, HttpSession session, OAuth2AuthenticationToken authentication) {
		 
		// TODO tratar de ver si se puede recuperar el token de alguna manera para usarlo donde se necesite.
		//repo.loadAuthorizedClient(clientRegistrationId, principal, request)
		 //return this.authorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(),
	     //           authentication.getName()).getAccessToken().getTokenValue();
		
		LOG.info("parameter principal: "+principal);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		LOG.info("Username (Principal): "+auth.getPrincipal());
		LOG.info("Credentials: "+auth.getCredentials());
		LOG.info("Roles: "+ auth.getAuthorities());
		LOG.info("Details: " + auth.getDetails());
		
		LOG.info("SessionId:" + RequestContextHolder.currentRequestAttributes().getSessionId());
		LOG.info("SessionId (parameter): " + session.getId());
		OAuth2AuthorizedClient client = authorizedClientService
			      .loadAuthorizedClient(
			        authentication.getAuthorizedClientRegistrationId(), 
			          authentication.getName());

		LOG.info("Access Token: " +client.getAccessToken());
		return auth.getPrincipal().toString();
	}
}
