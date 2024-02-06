package ticket.store.backend.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties (
        AccessTokenProperties accessToken,
        RefreshTokenProperties refreshToken
) {}
