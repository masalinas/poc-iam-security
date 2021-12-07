package io.oferto.security.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeycloakAdminApiConfig {
	static Keycloak keycloak = null;
	
    final static String serverUrl = "http://localhost:8080/auth";
    public final static String realm = "poc";
    //final static String realm = "poc";
    final static String clientId = "admin-cli";
    //final static String clientSecret = "01819628-948c-4220-9e6d-e56da5f34d56";
    final static String userName = "admin";
    final static String password = "admin";
    
    public KeycloakAdminApiConfig() {
    }

    public static Keycloak getInstance() {
        if(keycloak == null){           
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    //.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    //.clientSecret(clientSecret)                                       
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();
        }
        
        return keycloak;
    }
}
