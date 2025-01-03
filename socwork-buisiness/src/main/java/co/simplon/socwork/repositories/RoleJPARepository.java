package co.simplon.socwork.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import co.simplon.socwork.entities.Role;

public interface RoleJPARepository extends JpaRepository<Role, Long> {

	Set<Role> findByIsDefaultTrue();
}
