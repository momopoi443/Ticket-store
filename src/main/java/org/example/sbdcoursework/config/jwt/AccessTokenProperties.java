package org.example.sbdcoursework.config.jwt;

public record AccessTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTimeInMinutes
) {}
