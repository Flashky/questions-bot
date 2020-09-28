package com.xio91.bots.twitch.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Models a successful response from Twitch token endpoint.
 * <p>This response can be used for both 
 * <a href="https://dev.twitch.tv/docs/authentication#getting-tokens">getting</a> 
 * and
 * <a href="https://dev.twitch.tv/docs/authentication#refreshing-access-tokens">refreshing</a> a token.</p>
 * @author bruizv
 */
@Data
public class TokenResponse {

	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("expires_in")
	private Integer expirationSeconds;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	private Set<String> scopes;
	
	@JsonProperty("token_type")
	private String tokenType;
	
}
