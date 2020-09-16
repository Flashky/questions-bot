package com.xio91.bots.service;

public interface IrcClient {

	void connect(String oauthToken, String nick);
	void disconnect();
	void join(String channel);
	void part(String channel);
	boolean isConnected();
	
}
