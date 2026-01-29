package com.house.biet.auth.infrastructure.jwt;

import com.house.biet.user.command.domain.vo.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    JwtProvider jwtProvider;

    @Test
    @DisplayName("성공 - access token 생성 성공")
    void createAccessToken_Success() {
        // given
        Long accountId = 1L;
        String role = UserRole.USER.toString();

        // when
        String accessToken = jwtProvider.createAccessToken(accountId, role);

        // then
        assertThat(accessToken).isNotNull();
        assertThat(accessToken.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("성공 - access token 파싱 검증")
    void parseAccessToken_Claims() {
        // given
        Long accountId = 1L;
        String role = UserRole.USER.toString();
        String token = jwtProvider.createAccessToken(accountId, role);

        // when
        Claims claims = jwtProvider.parseAccessToken(token);

        // then
        assertThat(claims.getSubject()).isEqualTo(String.valueOf(accountId));
        assertThat(claims.get("role")).isEqualTo(role);
    }
}