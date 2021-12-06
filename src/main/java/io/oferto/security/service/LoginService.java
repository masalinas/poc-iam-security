package io.oferto.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import io.oferto.security.dto.LoginRequest;
import io.oferto.security.dto.LoginResponse;

@Service
public class LoginService {
	
    Logger logger = LoggerFactory.getLogger(LoginService.class);
                
    final static String clientId = "test";
           
    @Autowired
    RestTemplate restTemplate;
    
    public ResponseEntity<LoginResponse> login(String realm, LoginRequest request)  {    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("grant_type", "password");
        map.add("username", request.getUsername());
        map.add("password", request.getPassword());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("http://localhost:8080/auth/realms/" + realm + "/protocol/openid-connect/token", httpEntity, LoginResponse.class);        
        
        return ResponseEntity.status(200).body(loginResponse.getBody());
    }
    
    public ResponseEntity<String> logout(String realm, String refreshToken)  {    	
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id",clientId);
        map.add("refresh_token",refreshToken);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> logoutResponse = restTemplate.postForEntity("http://localhost:8080/auth/realms/" + realm + "/protocol/openid-connect/logout", httpEntity, String.class);
        
        return ResponseEntity.status(200).body(logoutResponse.getBody());
    }
}
