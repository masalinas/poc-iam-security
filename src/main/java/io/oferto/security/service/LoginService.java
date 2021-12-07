package io.oferto.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import io.oferto.security.dto.LoginRequest;
import io.oferto.security.dto.LoginResponse;
import io.oferto.security.dto.RefreshTokenRequest;

@Service
public class LoginService {
	
    Logger logger = LoggerFactory.getLogger(LoginService.class);
    
    final static String domain = "http://localhost:8080";
    final static String clientId = "admin-api";
           
    RestTemplate restTemplate;
    
    public LoginService(RestTemplate restTemplate) {
    	this.restTemplate = restTemplate;
    }
    
    public ResponseEntity<LoginResponse> login(String realm, LoginRequest request)  {    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("grant_type", "password");
        map.add("username", request.getUsername());
        map.add("password", request.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(domain + "/auth/realms/" + realm + "/protocol/openid-connect/token", httpEntity, LoginResponse.class);        
        
        return ResponseEntity.status(200).body(loginResponse.getBody());
    }
    
    public ResponseEntity<LoginResponse> refreshToken(String realm, RefreshTokenRequest request)  {    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", request.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(domain + "/auth/realms/" + realm + "/protocol/openid-connect/token", httpEntity, LoginResponse.class);        
        
        return ResponseEntity.status(200).body(loginResponse.getBody());
    }
    
    public ResponseEntity<String> logout(String realm, String refreshToken)  {    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id",clientId);
        map.add("refresh_token",refreshToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> logoutResponse = restTemplate.postForEntity(domain + "/auth/realms/" + realm + "/protocol/openid-connect/logout", httpEntity, String.class);
        
        return ResponseEntity.status(200).body(logoutResponse.getBody());
    }
}
