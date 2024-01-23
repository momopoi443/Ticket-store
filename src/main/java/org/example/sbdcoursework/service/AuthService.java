package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.auth.CredentialsDto;
import org.example.sbdcoursework.dto.auth.LoginResponseDto;

import java.util.UUID;

public interface AuthService {

    LoginResponseDto login(CredentialsDto credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}