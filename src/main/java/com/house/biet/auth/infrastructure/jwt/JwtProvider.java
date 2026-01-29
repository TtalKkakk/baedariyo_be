package com.house.biet.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;

    public JwtProvider(JwtProperties properties) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.accessTokenValiditySeconds = properties.getAccessTokenValiditySeconds();
        this.refreshTokenValiditySeconds = properties.getRefreshTokenValiditySeconds();
    }

    public String createAccessToken(Long accountId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(accountId))
                .claim("role", role)
                .setIssuedAt(now())
                .setExpiration(expireAfter(accessTokenValiditySeconds))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long accountId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(accountId))
                .claim("role", role)
                .setIssuedAt(now())
                .setExpiration(expireAfter(refreshTokenValiditySeconds))
                .signWith(secretKey)
                .compact();
    }

    private Date now() {
        return new Date();
    }

    private Date expireAfter(long millis) {
        return new Date(System.currentTimeMillis() + millis);
    }

    public Claims parseAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
