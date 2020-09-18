package com.xio91.bots.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authSuccessHandler;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	// TODO fix "/" being an permitted access point.
    	
    	// Problem 1: The Spring Boot OAuth 2.0 handshake finish at "/".
    	
    	// If that endpoint is unauthorized, the handshake enters in a redirect loop:
    	// http://localhost:8080/login 
    	// 		-> https://id.twitch.tv/oauth2/authorize?...
    	// 			-> http://localhost:8080/login/oauth2/code/
    	//				-> https://id.twitch.tv/oauth2/token
    	// 				-> https://id.twitch.tv/oauth2/userinfo
    	//			-> http://localhost:8080/ -> Not allowed -> http://localhost:8080/login (repeat)
    	
        http.authorizeRequests()
        	.antMatchers("/","/login**", 
        			"/webjars/**",
        			"/error**", 
        			"/oauth2/authorization/**",
        			"/oauth/code/*",
        			"/check-token").permitAll()
        		.antMatchers("/", "/irc/**")
        		.authenticated()
        .and()
        	.oauth2Login()
        	.successHandler(authSuccessHandler)
        .and()
        	.sessionManagement()
        	.maximumSessions(1)
        	.sessionRegistry(sessionRegistry())
        	.expiredUrl("/login");
        
        //.and()
        	//.sessionManagement().maximumSessions(1).sessionRegistry(new SessionRegistryImpl())
        	//.expiredSessionStrategy(new SimpleRedirectSessionInformationExpiredStrategy("login"));
        	
        
        	//.oauth2Client()
  	    //.and()
  	    //	.formLogin(); // Add this to use a form login for authenticating
        
    }

    // TODO let the following configure and passwordEncoder methods here to add formLogin in the future.
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser("user1").password(passwordEncoder().encode("user1")).roles("USER")
          .and()
          .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
