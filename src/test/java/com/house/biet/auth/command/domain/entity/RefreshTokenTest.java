package com.house.biet.auth.command.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    @Test
    @DisplayName("성공 - refreshToken 발급 성공")
    void issueRefreshToken_Success() {
        // given
        Long accountId = 1L;
        String token = "<REFRESH-TOKEN>";
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when
        RefreshToken refreshToken = RefreshToken.issue(accountId, token, expiredAt);

        // then
        assertThat(refreshToken.getAccountId()).isEqualTo(accountId);
        assertThat(refreshToken.getToken()).isEqualTo(token);
        assertThat(refreshToken.getExpiredAt()).isEqualTo(expiredAt);
    }

    @Test
    @DisplayName("성공 - refreshToken 만료 확인")
    void expired_refreshToken() {
        // given
        Long accountId = 1L;
        String token = "<REFRESH-TOKEN>";
        LocalDateTime expiredAt = LocalDateTime.now().minusSeconds(10);

        RefreshToken refreshToken = RefreshToken.issue(accountId, token, expiredAt);

        // when
        boolean expired = refreshToken.isExpired();

        // then
        assertThat(expired).isTrue();
    }

    @Test
    @DisplayName("성공 - refreshToken 미만료 확인")
    void notExpired_refreshToken() {
        // given
        Long accountId = 1L;
        String token = "<REFRESH-TOKEN>";
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        RefreshToken refreshToken = RefreshToken.issue(accountId, token, expiredAt);

        // when
        boolean expired = refreshToken.isExpired();

        // then
        assertThat(expired).isFalse();
    }
}