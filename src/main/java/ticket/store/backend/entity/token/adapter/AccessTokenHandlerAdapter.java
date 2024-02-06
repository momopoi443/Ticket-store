package ticket.store.backend.entity.token.adapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
import ticket.store.backend.entity.token.AccessToken;
import ticket.store.backend.entity.token.AccessTokenClaims;
import ticket.store.backend.entity.user.UserRole;

import java.util.UUID;

import static ticket.store.backend.service.TimeUtils.convertDateToLocalDateTime;

public class AccessTokenHandlerAdapter extends JwtHandlerAdapter<AccessToken> {

    @Override
    public AccessToken onClaimsJws(Jws<Claims> jws) {
        return new AccessToken(
                jws.getHeader(),
                new AccessTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(jws.getBody().getSubject()),
                        UserRole.valueOf(jws.getBody().get("role", String.class))
                )
        );
    }
}
