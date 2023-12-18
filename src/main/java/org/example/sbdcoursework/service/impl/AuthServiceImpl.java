package org.example.sbdcoursework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.sbdcoursework.dto.auth.CredentialsDTO;
import org.example.sbdcoursework.dto.auth.LoginResponsePayload;
import org.example.sbdcoursework.dto.auth.TokenPair;
import org.example.sbdcoursework.dto.user.UserDTO;
import org.example.sbdcoursework.entity.user.User;
import org.example.sbdcoursework.entity.token.RefreshToken;
import org.example.sbdcoursework.exception.InvalidTokenException;
import org.example.sbdcoursework.exception.InvalidUserCredentialsException;
import org.example.sbdcoursework.mapper.UserMapper;
import org.example.sbdcoursework.repository.UserRepository;
import org.example.sbdcoursework.service.AuthService;
import org.example.sbdcoursework.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponsePayload login(CredentialsDTO credentials) {
        AtomicReference<User> user = new AtomicReference<>();

        userRepository.findByEmail(credentials.getEmail()).ifPresentOrElse(
                foundUser -> {
                    if (!passwordEncoder.matches(
                            credentials.getPassword(),
                            foundUser.getPassword()
                    )) {
                        logAndThrowException(
                                new InvalidUserCredentialsException("Wrong password")
                        );
                    } else {
                        user.set(foundUser);
                    }
                },
                () -> logAndThrowException(
                        new InvalidUserCredentialsException("Wrong email")
                )
        );

        TokenPair tokenPair = jwtService.issueTokenPair(user.get());
        UserDTO userDTO = userMapper.mapUserToUserDTO(user.get());

        log.info("User: " + user.get().getEmail() + " logged");
        return new LoginResponsePayload(
                userDTO,
                tokenPair
        );
    }

    @Override
    public String refresh(String refreshToken) {
        RefreshToken parsedRefreshToken = jwtService.parseRefreshToken(refreshToken);

        AtomicReference<User> user = new AtomicReference<>();
        userRepository.findByUuid(parsedRefreshToken.claims().sub()).ifPresentOrElse(
                user::set,
                () -> logAndThrowException(
                        new InvalidTokenException("Invalid subject: no such user with that id")
                )
        );

        log.info("User: " + user.get().getEmail() + " refreshed access token");
        return jwtService.issueAccessToken(user.get());
    }

    @Override
    public void logout(UUID userUuid) {
        jwtService.invalidateRefreshToken(userUuid);
        log.info("User: " + userUuid + " logged out");
    }

    private void logAndThrowException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        throw exception;
    }
}
