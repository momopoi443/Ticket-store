package org.example.sbdcoursework.entity.token.adapter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtHandlerAdapter;
import org.example.sbdcoursework.entity.token.AccessToken;
import org.example.sbdcoursework.entity.token.AccessTokenClaims;
import org.example.sbdcoursework.entity.user.UserRole;

import java.util.UUID;

import static org.example.sbdcoursework.service.TimeUtils.convertDateToLocalDateTime;

public class AccessTokenHandlerAdapter extends JwtHandlerAdapter<AccessToken> {

    @Override
    public AccessToken onClaimsJws(Jws<Claims> jws) {
        return new AccessToken(
                jws.getHeader(),
                new AccessTokenClaims(
                        convertDateToLocalDateTime(jws.getBody().getIssuedAt()),
                        convertDateToLocalDateTime(jws.getBody().getExpiration()),
                        UUID.fromString(
                                jws.getBody().getSubject()
                        ),
                        UserRole.valueOf(jws.getBody().get(
                                "role",
                                String.class
                        ))
                )
        );
    }
}
