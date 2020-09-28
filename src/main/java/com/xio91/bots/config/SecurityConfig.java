package com.xio91.bots.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authSuccessHandler;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	// TODO fix "/" being an permitted access point.
    	
        http.authorizeRequests()
        	.antMatchers("/login**", 
        			"/webjars/**",
        			"/error**", 
        			"/oauth2/authorization/**",
        			"/oauth/code/*", "/createQuestion").permitAll()
        	.antMatchers("/", "/connect")
        	.fullyAuthenticated()
        	//.authenticated()
        	
        .and()
        	.oauth2Login()
        	.successHandler(authSuccessHandler)
        .and()
        	.logout()
        	.logoutSuccessUrl("/")
        	.deleteCookies("JSESSIONID")
        	.clearAuthentication(true)
        	.invalidateHttpSession(true)
        .and()
        	.sessionManagement()
        	.maximumSessions(1)
        	.sessionRegistry(sessionRegistry())
        	.expiredUrl("/login");
        	
        
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }
    
    
    // OAuth2AuthorizedClientManager is needed for Okta Webclient outside of servlet context
    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService clientService)
    {

        OAuth2AuthorizedClientProvider authorizedClientProvider = 
            OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = 
            new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, clientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
    
    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager)
    {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 
        				= new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        
        oauth2.setDefaultClientRegistrationId("okta");
        
        return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
    }
    
    // Only works withing a servlet context request (@RestController):
    /*
    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrations, 
                				OAuth2AuthorizedClientRepository authorizedClients) {
        
    	ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 
    			= new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);
    	
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId("okta");
        
        return WebClient.builder()
            .apply(oauth2.oauth2Configuration())
            .build();
    }*/

}
