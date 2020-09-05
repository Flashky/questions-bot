package com.xio91.bots.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CallbackController {

	@Autowired
	private OAuth2ClientProperties oauth2ClientProperties;
	
	@Autowired
	private OAuth2AuthorizedClientService service; 
	
	@GetMapping("/")
	public String root() {
		return "root page";
	}

}
