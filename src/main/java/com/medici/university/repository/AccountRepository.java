package com.medici.university.repository;

import com.medici.university.entity.Account;
import com.medici.university.utils.object.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	Optional<Account> findByUsername(String username);

	Optional<Account> findByRoleAndUsername(Role role, String username);

	Optional<Account> findByRoleAndUsernameAndPassword(Role role, String username, String password);

}