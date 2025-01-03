package co.simplon.socwork.dtos;

import java.util.Map;
import java.util.Set;

import co.simplon.socwork.entities.Role;

public record AuthInfo(String token, Set<String> roles) {

	public AuthInfo(String token, Set<String> roles) {
		this.token= token;
		this.roles= roles;
	}

	
}
