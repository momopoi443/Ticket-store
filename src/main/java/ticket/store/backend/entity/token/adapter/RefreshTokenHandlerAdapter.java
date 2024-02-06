package ticket.store.backend.entity.token.adapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
import ticket.store.backend.entity.token.RefreshToken;
import ticket.store.backend.entity.token.RefreshTokenClaims;

import java.util.UUID;

import static ticket.store.backend.service.TimeUtils.convertDateToLocalDateTime;

public class RefreshTokenHandlerAdapter extends JwtHandlerAdapter<RefreshToken> {

    @Override
    public RefreshToken onClaimsJws(Jws<Claims> jws) {
        return new RefreshToken(
                jws.getHeader(),
                new RefreshTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(jws.getBody().getSubject())
                )
        );
    }
}
