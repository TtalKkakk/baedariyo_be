package com.house.biet.auth.infrastructure.jwt;

import com.house.biet.global.vo.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    JwtProvider jwtProvider;

    @BeforeEach
    void setup() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecretKey("test-secret-key-test-secret-key-test");
        jwtProperties.setAccessTokenValiditySeconds(8640000);
        jwtProperties.setRefreshTokenValiditySeconds(8640000);

        jwtProvider = new JwtProvider(jwtProperties);
    }

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