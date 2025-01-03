package co.simplon.socwork.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.simplon.socwork.dtos.AccountCreate;
import co.simplon.socwork.dtos.AuthInfo;
import co.simplon.socwork.dtos.Login;
import co.simplon.socwork.entities.Account;
import co.simplon.socwork.services.AccountService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	private final AccountService service;
	
	public AccountController(AccountService service) {
		this.service = service;
	}
	
	@GetMapping
	String getToken() {
		return "Get";
	}
	
	@GetMapping("/with-role")
	Object withRole() {
		return "with role";
	}
	
	@GetMapping("/get-account/{id}")
	Account getAccount(@PathVariable("id") int id) {
		return service.getOneAccount(id);
	}
	
	
	@PostMapping //on n'a pas besoin de mettre "creer-compte" car c'est evident que method POST + accounts dans url Ca veut dire que tu cree une collection de ressource accounts donc pas besoin de dire qu'on cree un compte dans un url
	@ResponseStatus(HttpStatus.CREATED)
	void create(@RequestBody AccountCreate inputs) {
		
		service.create(inputs);
	}
	
	//base64 reversible
	
	//creer deux dto car la validation sont differents. pour login on n'a pas besoin de validation comme create
	//HttpStatus.CREATED car on cree token
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.CREATED)
	AuthInfo signin(@RequestBody @Valid Login inputs) {
		return service.signin(inputs);
	}
	

}
