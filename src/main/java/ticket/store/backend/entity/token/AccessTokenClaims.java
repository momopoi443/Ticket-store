package ticket.store.backend.entity.token;

import ticket.store.backend.entity.user.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub,
        UserRole role
) {}
