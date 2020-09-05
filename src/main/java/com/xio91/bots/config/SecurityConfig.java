package com.xio91.bots.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        	.authorizeRequests()
        	.antMatchers("/home", "/login**","/callback", "/webjars/**", "/error**", "/oauth2/authorization/**", "/oauth2/authorize")
        	.permitAll()
        	.anyRequest()
        	.authenticated()
        .and()
        	.oauth2Login();
        
    }

}
