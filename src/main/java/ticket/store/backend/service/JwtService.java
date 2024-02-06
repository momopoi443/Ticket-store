package ticket.store.backend.service;

import ticket.store.backend.dto.auth.TokenPair;
import ticket.store.backend.entity.user.User;
import ticket.store.backend.entity.token.AccessToken;
import ticket.store.backend.entity.token.RefreshToken;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    TokenPair issueTokenPair(User user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
