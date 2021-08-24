package com.medici.university.service;

import com.medici.university.utils.object.RegisterGeneric;

public interface PublicService {
    String register(RegisterGeneric data);

    String loginStudent(String username, String password);

    String loginProfessor(String username, String password);

    void resetStudent(String username);

    void resetProfessor(String username);
}
