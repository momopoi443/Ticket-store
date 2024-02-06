package ticket.store.backend.config.jwt;

public record AccessTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTimeInMinutes
) {}
