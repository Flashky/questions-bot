package com.xio91.bots.twitch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Models a successful response from Twitch <a href="https://dev.twitch.tv/docs/authentication#validating-requests">validation endpoint</a>.
 * @author bruizv
 */
@Data
public class ValidateResponse {

	@JsonProperty("client_id")
	private String clientId;
	
	private String login;
	
	private List<String> scopes;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("expires_in")
	private Integer expirationSeconds;
	
}
