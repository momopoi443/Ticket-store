package ticket.store.backend.entity.token;

import io.jsonwebtoken.Header;

public record AccessToken(
        Header header,
        AccessTokenClaims claims
) {}
