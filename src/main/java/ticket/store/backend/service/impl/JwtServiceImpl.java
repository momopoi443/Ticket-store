package ticket.store.backend.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import ticket.store.backend.config.jwt.JwtProperties;
import ticket.store.backend.dto.auth.TokenPair;
import ticket.store.backend.entity.user.User;
import ticket.store.backend.entity.token.AccessToken;
import ticket.store.backend.entity.token.RefreshToken;
import ticket.store.backend.entity.token.adapter.AccessTokenHandlerAdapter;
import ticket.store.backend.entity.token.adapter.RefreshTokenHandlerAdapter;
import ticket.store.backend.exception.external.InvalidTokenException;
import ticket.store.backend.service.JwtService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ticket.store.backend.service.KeyUtils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static ticket.store.backend.service.TimeUtils.convertLocalDateTimeToDate;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private final PrivateKey refreshTokenPrivateKey;
    private final JwtParser refreshTokenParser;
    private final RefreshTokenHandlerAdapter refreshTokenHandlerAdapter;

    private final PrivateKey accessTokenPrivateKey;
    private final JwtParser accessTokenParser;
    private final AccessTokenHandlerAdapter accessTokenHandlerAdapter;

    private final RedisTemplate<UUID, String> refreshTokenRedisTemplate;

    public JwtServiceImpl(
            JwtProperties jwtProperties,
            RedisTemplate<UUID, String> refreshTokenRedisTemplate
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        this.jwtProperties = jwtProperties;

        refreshTokenPrivateKey = KeyUtils.convertBase64PrivateKeyToJavaPrivateKey(
                jwtProperties.refreshToken().secretKey(),
                keyFactory
        );
        PublicKey refreshTokenPublicKey = KeyUtils.convertBase64PublicKeyToJavaPublicKey(
                jwtProperties.refreshToken().publicKey(),
                keyFactory
        );
        refreshTokenParser = Jwts.parserBuilder()
                .setSigningKey(refreshTokenPublicKey)
                .build();
        refreshTokenHandlerAdapter = new RefreshTokenHandlerAdapter();

        accessTokenPrivateKey = KeyUtils.convertBase64PrivateKeyToJavaPrivateKey(
                jwtProperties.accessToken().secretKey(),
                keyFactory
        );
        PublicKey accessTokenPublicKey = KeyUtils.convertBase64PublicKeyToJavaPublicKey(
                jwtProperties.accessToken().publicKey(),
                keyFactory
        );
        accessTokenParser = Jwts.parserBuilder()
                .setSigningKey(accessTokenPublicKey)
                .build();
        accessTokenHandlerAdapter = new AccessTokenHandlerAdapter();

        this.refreshTokenRedisTemplate = refreshTokenRedisTemplate;
    }

    @Override
    public String issueAccessToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now().withNano(0);
        LocalDateTime expirationTime = issuedTime.plusMinutes(jwtProperties.accessToken().expirationTimeInMinutes());

        String builtAccessToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .claim("role", user.getRole())
                .signWith(accessTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        log.info("Issued access token for user with email: [" + user.getEmail() + "]");
        return builtAccessToken;
    }

    @Override
    public String issueRefreshToken(User user) {
        LocalDateTime issuedTime = LocalDateTime.now().withNano(0);
        LocalDateTime expirationTime = issuedTime.plusMinutes(
                jwtProperties.refreshToken().expirationTimeInMinutes()
        );

        String builtRefreshToken = Jwts.builder()
                .setIssuedAt(convertLocalDateTimeToDate(issuedTime))
                .setExpiration(convertLocalDateTimeToDate(expirationTime))
                .setSubject(user.getUuid().toString())
                .signWith(refreshTokenPrivateKey, SignatureAlgorithm.RS256)
                .compact();

        refreshTokenRedisTemplate.opsForValue().set(
                user.getUuid(),
                builtRefreshToken,
                jwtProperties.refreshToken().expirationTimeInMinutes(),
                TimeUnit.MINUTES
        );

        log.info("Issued refresh token for user with email: [" + user.getEmail() + "]");
        return builtRefreshToken;
    }

    @Override
    public TokenPair issueTokenPair(User user) {
        String accessToken = issueAccessToken(user);
        String refreshToken = issueRefreshToken(user);

        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public void invalidateRefreshToken(UUID userUuid) {
        refreshTokenRedisTemplate.delete(userUuid);
        log.info("Invalidated refresh token for user with uuid: [" + userUuid + "]");
    }

    @Override
    public AccessToken parseAccessToken(String accessToken) {
        try {
            return accessTokenParser.parse(accessToken, accessTokenHandlerAdapter);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Given access token has expired", e);
        } catch (RuntimeException e) {
            throw new InvalidTokenException("Given invalid access token", e);
        }
    }

    @Override
    public RefreshToken parseRefreshToken(String refreshToken) {
        RefreshToken parsedRefreshToken;

        try {
            parsedRefreshToken = refreshTokenParser.parse(refreshToken, refreshTokenHandlerAdapter);
        } catch (RuntimeException e) {
            throw new InvalidTokenException("Given invalid refresh token", e);
        }

        if (Boolean.FALSE.equals(refreshTokenRedisTemplate.hasKey(parsedRefreshToken.claims().sub()))) {
            throw new InvalidTokenException("Given refresh token has expired");
        }

        return parsedRefreshToken;
    }
}
