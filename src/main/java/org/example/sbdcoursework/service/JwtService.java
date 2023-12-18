package org.example.sbdcoursework.service;

import org.example.sbdcoursework.dto.auth.TokenPair;
import org.example.sbdcoursework.entity.user.User;
import org.example.sbdcoursework.entity.token.AccessToken;
import org.example.sbdcoursework.entity.token.RefreshToken;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    TokenPair issueTokenPair(User user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
