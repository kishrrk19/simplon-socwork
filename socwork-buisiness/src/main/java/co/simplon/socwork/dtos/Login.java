package co.simplon.socwork.dtos;

import jakarta.validation.constraints.NotBlank;

public record Login(@NotBlank String username, @NotBlank String password) {

	
	@Override
	public String toString() {
		return "AccountCreate [username=" + username + ", password= [PROTECTED]"  + "]";
	}

	
}
