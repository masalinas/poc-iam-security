package io.oferto.security.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UserRequest {
	private boolean enabled;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private Map<String, List<String>> attributes;
}
