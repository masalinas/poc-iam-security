package io.oferto.security.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.oferto.security.config.KeycloakAdminApiConfig;
import io.oferto.security.dto.LoginRequest;
import io.oferto.security.dto.LoginResponse;
import io.oferto.security.dto.RefreshTokenRequest;
import io.oferto.security.dto.UserRequest;
import io.oferto.security.service.LoginService;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

@RestController
@RequestMapping("iam")
public class UserController {
	final String CLIENT_NAME = "poc";	
	final Logger log = LoggerFactory.getLogger(UserController.class);
    
    LoginService loginService;
    
    public UserController(LoginService loginService) {
    	this.loginService = loginService;
    }
    
    @PostMapping("/{realm}/login")
    public ResponseEntity<LoginResponse> login(@PathVariable("realm") String realm, @RequestBody LoginRequest loginRequest) throws Exception {
        log.info("Executing Login");
                        
        ResponseEntity<LoginResponse> response = loginService.login(realm, loginRequest);

        return response;
    }
    
    @PostMapping("/{realm}/refresh")
    public ResponseEntity<LoginResponse> refresh(@PathVariable("realm") String realm, @RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        log.info("Executing Refresh");
                        
        ResponseEntity<LoginResponse> response = loginService.refreshToken(realm, refreshTokenRequest);

        return response;
    }
    
    @PostMapping("/{realm}/users/{id}/resetPassword")
    public void resetPassword(@PathVariable("realm") String realm, @PathVariable("id") String id, @RequestBody String resetPasswordRequest) throws Exception {
        log.info("Executing reset Password");
                        
        RealmResource realmResource = KeycloakAdminApiConfig.getInstance().realm(realm);
		UsersResource usersResource = realmResource.users();

		UserResource userResource = usersResource.get(id);
		
	    // set user password
	    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
	    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
	    credentialRepresentation.setTemporary(false);
	    credentialRepresentation.setValue(resetPasswordRequest);
	    	    
	    userResource.resetPassword(credentialRepresentation);	    
    }
    
    @PostMapping("/{realm}/logout")
    public ResponseEntity<String> logout(@PathVariable("realm") String realm, @RequestBody String refreshToken) throws Exception {
        log.info("Executing Logout");
                        
        ResponseEntity<String> response = loginService.logout(realm, refreshToken);

        return response;
    }
    
    @PreAuthorize("hasAnyRole('admin')")
	@RequestMapping(value = "/{realm}/clients", method = RequestMethod.GET)
    public List<ClientRepresentation> getClients(@PathVariable("realm") String realm) throws Exception {
        log.info("Executing get Clients");
                    	
    	ClientsResource clientsResource = KeycloakAdminApiConfig.getInstance().realm(realm).clients();
    	
    	return clientsResource.findAll();
    }
		
    @PreAuthorize("hasAnyRole('admin','operator', 'user')")
	@RequestMapping(value = "/{realm}/users", method = RequestMethod.GET)
    public List<UserRepresentation> getUsers(@PathVariable("realm") String realm) throws Exception {
        log.info("Executing get Users");
                
    	UsersResource usersResource = KeycloakAdminApiConfig.getInstance().realm(realm).users();    	
    	
    	return usersResource.list();    	    	
    }
	
    @PreAuthorize("hasAnyRole('admin','operator', 'user')")
	@RequestMapping(value = "/{realm}/users/{id}", method = RequestMethod.GET)
    public UserRepresentation getUserById(@PathVariable("realm") String realm, @PathVariable("id") String id) throws Exception {
        log.info("Executing get User by Id");
                
    	UsersResource usersResource = KeycloakAdminApiConfig.getInstance().realm(realm).users();
    	
    	    	    
    	return usersResource.get(id).toRepresentation();       
    }
	
    @PreAuthorize("hasAnyRole('admin','operator', 'user')")
	@RequestMapping(value = "/{realm}/users/{id}/roles", method = RequestMethod.GET)
    public MappingsRepresentation getRoles(@PathVariable("realm") String realm, @PathVariable("id") String id) throws Exception {
        log.info("Executing get Roles");
                    	
    	UsersResource usersResource = KeycloakAdminApiConfig.getInstance().realm(realm).users();

    	return usersResource.get(id).roles().getAll();
    }
	
    @PreAuthorize("hasAnyRole('admin', 'operator')")
	@RequestMapping(value = "/{realm}/users", method = RequestMethod.POST)
	public String createUser(@PathVariable("realm") String realm, @RequestBody UserRequest userRequest) {
		log.info("Executing create User");
		
		RealmResource realmResource = KeycloakAdminApiConfig.getInstance().realm(realm);
		UsersResource usersResource = realmResource.users();
		
	   	UserRepresentation user = new UserRepresentation();
	    user.setUsername(userRequest.getUsername());
	    user.setFirstName(userRequest.getFirstName());
	    user.setLastName(userRequest.getLastName());
	    user.setEmail(userRequest.getEmail());
	    user.setCredentials(new ArrayList<>());
	    user.setEnabled(userRequest.isEnabled());
	    
	    for (Entry<String, List<String>> entry : userRequest.getAttributes().entrySet()) {	    	
	    	user.setAttributes(Collections.singletonMap(entry.getKey(), entry.getValue()));
	    }
	    	         
	    // create user
	    Response response = usersResource.create(user);
	    		    
	    // get user resource
	    String userId = CreatedResponseUtil.getCreatedId(response);
	    UserResource userResource = usersResource.get(userId);
	    	    	    	    
	    // assign Realm Roles to user
	    if (userRequest.getRealmRoles() != null) {
		    List<RoleRepresentation> roleRealmRepresentationAvailable = userResource.roles().realmLevel().listAvailable();
		    List<RoleRepresentation> roleRealmRepresentationRequest = new ArrayList<RoleRepresentation>();
		    
		    // set realm roles available to user
		    for (String userRole : userRequest.getRealmRoles()) {
			    for (RoleRepresentation roleRepresentation : roleRealmRepresentationAvailable) {
			      if (roleRepresentation.getName().equals(userRole)) {
			    	  roleRealmRepresentationRequest.add(roleRepresentation);		          		         
			       }
			    }
		    }
		    
		    // add realm roles to user
		    userResource.roles().realmLevel().add(roleRealmRepresentationRequest);
	    }
	    
	    // assign Client Roles to user
	    if (userRequest.getClientRoles() != null) {	    	    
		    List<RoleRepresentation> roleClientRepresentationRequest = new ArrayList<RoleRepresentation>();
		    
		    for (Entry<String, List<String>> entry : userRequest.getClientRoles().entrySet()) {
			    // get client Id by name
		    	List<ClientRepresentation> clientRepresentations =  realmResource.clients().findByClientId(entry.getKey());
		    			
		    	if (clientRepresentations.size() > 0) {
			    	ClientRepresentation clientRepresentation = realmResource.clients().findByClientId(entry.getKey()).get(0);
				    String clientId = clientRepresentation.getId();
			    	
				    // get client available roles
			    	List<RoleRepresentation> roleClientRepresentationAvailable = userResource.roles().clientLevel(clientId).listAvailable();
			    	
			    	// set client roles to user
			    	for (RoleRepresentation roleRepresentation : roleClientRepresentationAvailable) {
			    		for (String clientRole : entry.getValue()) {	
			    			if (roleRepresentation.getName().equals(clientRole)) {				    	  
						    	 roleClientRepresentationRequest.add(roleRepresentation);
						    }
			    		}
			    	}
		    			    
			    	// add client roles to user
			    	userResource.roles().clientLevel(clientId).add(roleClientRepresentationRequest);
		    	}
		    }		    
	    }
	    	    
	    // set user password
	    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
	    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
	    credentialRepresentation.setTemporary(false);
	    credentialRepresentation.setValue(userRequest.getPassword());
	    	    
	    userResource.resetPassword(credentialRepresentation);
	    
	    return userId;	    
	}

    @PreAuthorize("hasAnyRole('admin', 'operator')")
	@RequestMapping(value = "/{realm}/users/{id}", method = RequestMethod.PUT)
	public void updateUser(@PathVariable("realm") String realm, @PathVariable("id") String id, @RequestBody UserRequest userRequest) {
		log.info("Executing update User");
		
		UsersResource usersResource = KeycloakAdminApiConfig.getInstance().realm(realm).users();
			 
		UserResource userResource = usersResource.get(id);
		
		UserRepresentation user = new UserRepresentation();
	    user.setEnabled(userRequest.isEnabled());
	    user.setUsername(userRequest.getUsername());
	    user.setFirstName(userRequest.getFirstName());
	    user.setLastName(userRequest.getLastName());
	    user.setEmail(userRequest.getEmail());
	    
	    for (Entry<String, List<String>> entry : userRequest.getAttributes().entrySet()) {	    	
	    	user.setAttributes(Collections.singletonMap(entry.getKey(), entry.getValue()));
	    }
	    
	    userResource.update(user);
	}
	
    @PreAuthorize("hasAnyRole('admin', 'operator')")
	@RequestMapping(value = "/{realm}/users/{id}", method = RequestMethod.DELETE)
	public int deleteUser(@PathVariable("realm") String realm, @PathVariable("id") String id) {
		log.info("Executing delete User");
		
		UsersResource usersResource = KeycloakAdminApiConfig.getInstance().realm(realm).users();
			    	         
	    Response response = usersResource.delete(id);
	    	 
	    return response.getStatus();
	}
}
