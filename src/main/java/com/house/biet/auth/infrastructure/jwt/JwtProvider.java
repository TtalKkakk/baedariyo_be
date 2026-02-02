package com.house.biet.auth.infrastructure.jwt;

import com.house.biet.global.vo.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private static final String ROLE_CLAIM = "role";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final Key secretKey;
    private final long accessTokenValiditySeconds;
    private final long refreshTokenValiditySeconds;

    public JwtProvider(JwtProperties properties) {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.accessTokenValiditySeconds = properties.getAccessTokenValiditySeconds();
        this.refreshTokenValiditySeconds = properties.getRefreshTokenValiditySeconds();
    }

    /* =====================
       Token Create
       ===================== */

    public String createAccessToken(Long accountId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(accountId))
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(now())
                .setExpiration(expireAfter(accessTokenValiditySeconds))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(Long accountId, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(accountId))
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(now())
                .setExpiration(expireAfter(refreshTokenValiditySeconds))
                .signWith(secretKey)
                .compact();
    }

    /* =====================
       Token Parse
       ===================== */

    public Claims parseAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getAccountId(String token) {
        return Long.valueOf(parseAccessToken(token).getSubject());
    }

    public UserRole getRole(String token) {
        String role = parseAccessToken(token).get(ROLE_CLAIM, String.class);
        return UserRole.valueOf(role);
    }

    /* =====================
       Validation
       ===================== */

    public boolean validateToken(String token) {
        try {
            parseAccessToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* =====================
       Resolve
       ===================== */

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /* =====================
       Time utils
       ===================== */

    private Date now() {
        return new Date();
    }

    private Date expireAfter(long millis) {
        return new Date(System.currentTimeMillis() + millis);
    }
}
