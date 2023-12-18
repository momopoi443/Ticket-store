package org.example.sbdcoursework.config.jwt;

public record RefreshTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTimeInMinutes
) {}
