package co.simplon.socwork.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.simplon.socwork.entities.Account;

@Repository
public interface AccountJPARepository extends JpaRepository<Account, Long> {

	//Optional<Account> isPresent()
	//Optional wo tsukaukotode null ganakunaru
	Optional<Account> findByUsernameIgnoreCase(String inputsUsername);

	Account getById(int id);

	Account findById(int id);


	
}
