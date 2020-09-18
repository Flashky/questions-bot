package com.xio91.bots.twitch.model;

import lombok.Data;

/**
 * Models unsuccessful responses from the Twitch API.
 * @author bruizv
 *
 */
@Data
public class ErrorResponse {

	private String status;
	private String message;
	
}
