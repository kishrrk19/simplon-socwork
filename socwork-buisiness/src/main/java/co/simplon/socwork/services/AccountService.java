package co.simplon.socwork.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.simplon.socwork.config.JwtProvider;
import co.simplon.socwork.dtos.AccountCreate;
import co.simplon.socwork.dtos.AuthInfo;
import co.simplon.socwork.dtos.Login;
import co.simplon.socwork.dtos.RoleInfo;
import co.simplon.socwork.entities.Account;
import co.simplon.socwork.entities.Role;
import co.simplon.socwork.repositories.AccountJPARepository;
import co.simplon.socwork.repositories.RoleJPARepository;

@Service
@Transactional(readOnly= true)
public class AccountService {
	
	private final AccountJPARepository accounts;
	private final RoleJPARepository roles;
	private final PasswordEncoder encoder;
	private final JwtProvider jwtProvider;
	
	public AccountService(AccountJPARepository accounts,RoleJPARepository roles, PasswordEncoder encoder, JwtProvider jwtProvider){
		this.accounts = accounts;
		this.roles = roles;
		this.encoder = encoder;
		this.jwtProvider = jwtProvider;
	};
	
	@Transactional
	public void create(AccountCreate inputs) {
		
//		Account account = new Account();
//		account.setUsername(inputs.username());
//		
//		String passencode = encoder.encode(inputs.password());
//		account.setPassword(passencode);
//		
		Set<Role> role = roles.findByIsDefaultTrue();
//		account.setRoles(role);
		
		String username = inputs.username();
		String passencode = encoder.encode(inputs.password());
		
		Account account = new Account(username, passencode, role);
		accounts.save(account);
		
	}
	
	public AuthInfo signin(Login inputs) {
		
		
		
//		Account account = new Account();
		String inputsUsername = inputs.username();
		
		//findByUsernameIgnoreCase
		//orElseThrow(lever exception)
		
		//si c'est null, il lance exceptioin Bad Credentials Exception. si c'est pas null, function flelche de BadCredentialsException ne sera pas apple
		Account entity = accounts.findByUsernameIgnoreCase(inputsUsername)
				.orElseThrow(()-> new BadCredentialsException(inputsUsername));
		// from here entity is never null(value of optional is nerver null) car si c'est null exception est levee
		//verify passwords
		/// ???
		// temporary return
		System.out.println(entity);
		System.out.println("roles=>" + entity.getRoles());
		
		Set<Role> roles= entity.getRoles();
		Set<String> roleInfos = new HashSet<>();
		
		for(Role role : roles) {
			String rolename =role.getAuthority();
			
//			RoleInfo roleInfoAdd = new RoleInfo(rolename);
			roleInfos.add(rolename);
//			roleInfos.add(roleInfoAdd);
			
		}
		
		
		//$version$tours$sel hash
		boolean compared = encoder.matches(inputs.password(), entity.getPassword());
		if(compared) {
			
			String tokenJWT = jwtProvider.create(inputsUsername, entity.getRoles());
//			Map<String, String> tokenJWT = new HashMap<>();
//			tokenJWT.put("token", jwtProvider.create(inputsUsername, entity.getRoles()));
			
			AuthInfo info = new AuthInfo(tokenJWT, roleInfos);
			
			return info; // goal of day
		}else {
			throw new BadCredentialsException(inputsUsername);
		}
		
	}
	
	public Account getOneAccount(int id) {
		
		Account account = accounts.findById(id);
		System.out.println(account);
		return account;
	}
	
	
}
