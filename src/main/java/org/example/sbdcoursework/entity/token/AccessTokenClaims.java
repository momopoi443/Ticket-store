package org.example.sbdcoursework.entity.token;

import org.example.sbdcoursework.entity.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub,
        UserRole role
) {}
