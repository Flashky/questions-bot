package com.xio91.bots.feign.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

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
