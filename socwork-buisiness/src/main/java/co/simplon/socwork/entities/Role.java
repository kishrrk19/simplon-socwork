package co.simplon.socwork.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name= "t_roles")
public final class Role extends AbstractEntity{

	@Column(name ="authority")
	private String authority;
	
	@Column(name ="default_role")
	private boolean isDefault;
	
//	@ManyToMany(mappedBy = "roles", cascade = CascadeType.PERSIST)
//    private Set<Account> accounts = new HashSet<>();
	
	public Role() {
	    // デフォルトコンストラクタ
	    this.authority = null;
	    this.isDefault = false;
	}

	
	public Role(String authority, Boolean isDefault) {
		Objects.requireNonNull(authority);
		Objects.requireNonNull(isDefault);
		this.authority= authority;
		this.isDefault= isDefault;
	}
	
	public String getAuthority() {
		return authority;
	}

//	public void setAuthority(String authority) {
//		this.authority = authority;
//	}

	public boolean getIsDefault() {
		return isDefault;
	}

//	public void setDefault(boolean isDefault) {
//		this.isDefault = isDefault;
//	}

//	public Set<Account> getAccounts() {
//		return accounts;
//	}
//
//	public void setAccounts(Set<Account> accounts) {
//		this.accounts = accounts;
//	}

	
	
	@Override
	public String toString() {
		return "Role [authority=" + authority + ", isDefault=" + isDefault + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(authority);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		Role other = (Role) obj;
		return authority.equals(other.authority);
	}
	
	
}
