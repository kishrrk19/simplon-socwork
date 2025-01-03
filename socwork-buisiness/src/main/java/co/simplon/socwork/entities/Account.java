package co.simplon.socwork.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "t_accounts")
public class Account extends AbstractEntity{

	@Column(name="username")
	private String username;
	
	@Column(name= "password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER) //pour deactiver lazy loading
    @JoinTable(
        name = "t_associate",
        joinColumns = @JoinColumn(name = "associate_account_id"),
        inverseJoinColumns = @JoinColumn(name = "associate_role_id")
    )
    private Set<Role> roles = new HashSet<>();

//	public Account(String username, String password) {
//		this(username, password, new HashSet<Role>());
//	}
	
	public Account() {
		this.username= null;
		this.password= null;
		this.roles = null;
	}
	
	public Account(String username, String password, Set<Role> roles) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		Objects.requireNonNull(roles);
		
		this.username= username;
		this.password= password;
		this.roles = new HashSet<>();
		for (Role role : roles) {
			addRole(role);
		}
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(roles);
	}
	
	public void addRole(Role role) {
		Objects.requireNonNull(role);
		roles.add(role);
	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		return username.equals(other.username);
	}

	//car par default orm lazyload les collections et chaque fois toString ser applee et ca fait moins de performance
	@Override
	public String toString() {
		return "Account [username=" + username + ", password=PROTECTED roles = LAZY_LOADED]";
	}
	
	
}
