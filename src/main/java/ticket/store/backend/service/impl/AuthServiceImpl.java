package ticket.store.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ticket.store.backend.dto.auth.CredentialsDto;
import ticket.store.backend.dto.auth.LoginResponseDto;
import ticket.store.backend.dto.auth.TokenPair;
import ticket.store.backend.dto.user.UserDto;
import ticket.store.backend.entity.token.RefreshToken;
import ticket.store.backend.entity.user.User;
import ticket.store.backend.exception.external.InvalidTokenException;
import ticket.store.backend.exception.external.InvalidUserCredentialsException;
import ticket.store.backend.mapper.UserMapper;
import ticket.store.backend.repository.UserRepository;
import ticket.store.backend.service.AuthService;
import ticket.store.backend.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(CredentialsDto credentials) {
        User foundUser = userRepository.findByEmail(credentials.getEmail())
                .orElseThrow(() -> new InvalidUserCredentialsException("Wrong email"));
        if (!passwordEncoder.matches(credentials.getPassword(), foundUser.getPassword())) {
            throw new InvalidUserCredentialsException("Wrong password");
        }

        TokenPair tokenPair = jwtService.issueTokenPair(foundUser);
        UserDto userDto = userMapper.mapUserToUserDTO(foundUser);

        log.info("User with email: [" + foundUser.getEmail() + "] logged in");
        return new LoginResponseDto(userDto, tokenPair);
    }

    @Override
    public String refresh(String refreshToken) {
        RefreshToken parsedRefreshToken = jwtService.parseRefreshToken(refreshToken);
        User foundUser = userRepository.findByUuid(parsedRefreshToken.claims().sub())
                .orElseThrow(() -> new InvalidTokenException("Invalid subject: no users with id + " + parsedRefreshToken.claims().sub()));

        log.info("User with uuid: [" + foundUser.getUuid() + "] refreshed access token");
        return jwtService.issueAccessToken(foundUser);
    }

    @Override
    public void logout(UUID userUuid) {
        jwtService.invalidateRefreshToken(userUuid);
        log.info("User with uuid: [" + userUuid + "] is logged out");
    }
}
