package ticket.store.backend.service;

import ticket.store.backend.dto.auth.CredentialsDto;
import ticket.store.backend.dto.auth.LoginResponseDto;

import java.util.UUID;

public interface AuthService {

    LoginResponseDto login(CredentialsDto credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}