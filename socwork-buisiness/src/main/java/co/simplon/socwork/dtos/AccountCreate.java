package co.simplon.socwork.dtos;

public record AccountCreate(String username, String password) {

	
	@Override
	public String toString() {
		return "AccountCreate [username=" + username + ", password= [PROTECTED]"  + "]";
	}

	
}
