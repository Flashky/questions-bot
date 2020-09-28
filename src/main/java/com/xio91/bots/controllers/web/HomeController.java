package com.xio91.bots.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xio91.bots.properties.TwitchIRCProperties;
import com.xio91.bots.services.IrcClient;

@Controller
public class HomeController {

	@Autowired
	private IrcClient questionBot;
	
    @Autowired 
    private TwitchIRCProperties twitchIrcProperties;
    
	@GetMapping("/")
	public ModelAndView getHome(Authentication authentication) {
		
		ModelAndView mav = new ModelAndView("home");

		mav.addObject("principalIsBot", twitchIrcProperties.getNick().equals(authentication.getName()));
		mav.addObject("isBotConnected", questionBot.isConnected());
		
		return mav;
	}
	

}
