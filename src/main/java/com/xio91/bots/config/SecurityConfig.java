package com.xio91.bots.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;


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
        			"/oauth/code/*").permitAll()
        	.antMatchers("/").authenticated()
        	
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
}
