package com.medici.university.service.impl;

import com.medici.university.service.PublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.medici.university.service.authentication.ProfessorAuthenticationService;
import com.medici.university.service.authentication.StudentAuthenticationService;
import com.medici.university.utils.object.RegisterGeneric;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PublicServiceImpl implements PublicService {
	private final StudentAuthenticationService studentAuthenticationService;
	private final ProfessorAuthenticationService professorAuthenticationService;

	@Override
	public String register(RegisterGeneric data) {
		return studentAuthenticationService.register(data);
	}


	@Override
	public String loginStudent(String username, String password) {
		return studentAuthenticationService.login(username, DigestUtils.sha3_256Hex(password));
	}

	@Override
	public String loginProfessor(String username, String password) {
		return professorAuthenticationService.login(username, DigestUtils.sha3_256Hex(password));
	}


	@Override
	public void resetStudent(String username) {
		studentAuthenticationService.reset(username);
	}

	@Override
	public void resetProfessor(String username) {
		professorAuthenticationService.reset(username);
	}
}
