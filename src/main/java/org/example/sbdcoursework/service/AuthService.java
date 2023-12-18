package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.auth.CredentialsDTO;
import org.example.sbdcoursework.dto.auth.LoginResponsePayload;

import java.util.UUID;

public interface AuthService {

    LoginResponsePayload login(CredentialsDTO credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}