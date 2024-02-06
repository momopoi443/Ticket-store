package ticket.store.backend.config.jwt;

public record RefreshTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTimeInMinutes
) {}
