package com.xio91.bots.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.xio91.bots.feign.dto.TokenResponse;
import com.xio91.bots.feign.dto.TokenStatusResponse;


@FeignClient(name = "twitch-oauth", url = "https://id.twitch.tv/oauth2")
public interface TwitchOAuth2Client {

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    TokenResponse getToken(@RequestBody Map<String, ?> form);
    
    @GetMapping("/validate")
    TokenStatusResponse getTokenStatus(@RequestHeader("Authorization") String oauthToken);
    
    @PostMapping("/revoke")
    void revokeToken(@RequestParam("client_id") String clientId, @RequestParam String token);
    
    
}
