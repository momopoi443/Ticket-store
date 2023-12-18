package org.example.sbdcoursework.entity.token.adapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
import org.example.sbdcoursework.entity.token.RefreshToken;
import org.example.sbdcoursework.entity.token.RefreshTokenClaims;

import java.util.UUID;

import static org.example.sbdcoursework.service.TimeUtils.convertDateToLocalDateTime;

public class RefreshTokenHandlerAdapter extends JwtHandlerAdapter<RefreshToken> {

    @Override
    public RefreshToken onClaimsJws(Jws<Claims> jws) {
        return new RefreshToken(
                jws.getHeader(),
                new RefreshTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(
                                jws.getBody().getSubject()
                        )
                )
        );
    }
}
