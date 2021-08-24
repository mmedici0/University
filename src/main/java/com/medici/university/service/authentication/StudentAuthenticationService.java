package com.medici.university.service.authentication;

import lombok.RequiredArgsConstructor;
import com.medici.university.repository.AccountRepository;
import com.medici.university.repository.StudentRepository;
import com.medici.university.service.jwt.JWTService;
import com.medici.university.entity.Account;
import com.medici.university.entity.Student;
import com.medici.university.service.jwt.JWTAuthenticationService;
import com.medici.university.utils.object.RegisterGeneric;
import com.medici.university.utils.object.Role;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.medici.university.utils.object.Role.Student;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StudentAuthenticationService {
	private final JWTService jwtService;
	private final AccountRepository accountRepository;
	private final StudentRepository studentRepository;
	private final JWTAuthenticationService authenticationService;

	public String login(String username, String password) {
		return accountRepository
				.findByRoleAndUsernameAndPassword(Role.Student, username, password)
				.map(user -> jwtService.create(Role.Student, username, password))
				.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
	}

	public void reset(String username) {
		authenticationService.reset(Role.Student, username);
	}

	public void changePassword(String username, String oldPassword, String newPassword) {
		authenticationService.changePassword(Student, username, oldPassword, newPassword);
	}

	public String register(RegisterGeneric data) {
		// All compiled

		if (data.hasNull()) {
			throwIllegal("Missing records");
		}

		// Email validate

		String pattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,})$";
		String email = data.getUsername().toLowerCase();
		if (!email.matches(pattern) || email.length() > 120) {
			throwIllegal("Invalid email");
		}

		// Existing account validate

		accountRepository.findByUsername(email).ifPresent((a) -> {
			throw throwIllegalReturn("Used email");
		});

		// Password validate

		pattern = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(.{8,30})$";
		if (!data.getPassword().matches(pattern)) {
			throwIllegal("Invalid password");
		}

		// Length validate

		if (!data.validLength()) {
			throwIllegal("Invalid records length");
		}

		// Save all

		Role role = Role.Student;
		String shaPassword = DigestUtils.sha3_256Hex(data.getPassword());

		Account account = accountRepository.save(
				new Account(email, shaPassword, role));
		studentRepository.save(
				new Student(account.getId(), data.getName(), data.getSurname()));

		return authenticationService.login(role, email, shaPassword);
	}

	private void throwIllegal(String msg) throws IllegalArgumentException {
		throw throwIllegalReturn(msg);
	}

	private IllegalArgumentException throwIllegalReturn(String msg) {
		return new IllegalArgumentException(msg);
	}
}
