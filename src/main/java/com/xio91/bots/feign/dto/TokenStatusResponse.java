package com.xio91.bots.feign.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TokenStatusResponse {

	@JsonProperty("client_id")
	private String clientId;
	
	private String login;
	
	@JsonProperty("expires_in")
	private Integer expirationSeconds;
	
	private List<String> scopes;
	
	@JsonProperty("user_id")
	private String userId;
	
}
