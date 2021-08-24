package com.medici.university.service.authentication;

import lombok.RequiredArgsConstructor;
import com.medici.university.repository.AccountRepository;
import com.medici.university.service.jwt.JWTAuthenticationService;
import com.medici.university.service.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.medici.university.utils.object.Role.Professor;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProfessorAuthenticationService {
	private final JWTService jwtService;
	private final AccountRepository accountRepository;
	private final JWTAuthenticationService authenticationService;

	public String login(String username, String password) {
		return accountRepository
				.findByRoleAndUsernameAndPassword(Professor, username, password)
				.map(user -> jwtService.create(Professor, username, password))
				.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
	}

	public void reset(String username) {
		authenticationService.reset(Professor, username);
	}

	public void changePassword(String username, String oldPassword, String newPassword) {
		authenticationService.changePassword(Professor, username, oldPassword, newPassword);
	}

}
