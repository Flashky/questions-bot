package com.xio91.bots.service;

public interface BotService {

	void connect(String oauthToken, String nick);
	void disconnect();
	
}
