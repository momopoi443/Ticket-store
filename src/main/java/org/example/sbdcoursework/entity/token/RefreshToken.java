package org.example.sbdcoursework.entity.token;

import io.jsonwebtoken.Header;

public record RefreshToken(
        Header header,
        RefreshTokenClaims claims
) {}
