package com.house.biet.auth.infrastructure.jwt;

import com.house.biet.common.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderConsistencyTest {

    @Test
    @DisplayName("tokens remain valid across providers with the same secret")
    void tokenCanBeValidatedAcrossProvidersWithSameSecret() {
        // given
        JwtProvider issuer = new JwtProvider(jwtProperties("abcdefghijklmnopqrstuvwxyz0123456789"));
        JwtProvider verifier = new JwtProvider(jwtProperties("abcdefghijklmnopqrstuvwxyz0123456789"));
        String token = issuer.createAccessToken(1L, UserRole.USER.name());

        // when
        boolean valid = verifier.validateToken(token);
        Long accountId = verifier.getAccountId(token);
        UserRole role = verifier.getRole(token);

        // then
        assertThat(valid).isTrue();
        assertThat(accountId).isEqualTo(1L);
        assertThat(role).isEqualTo(UserRole.USER);
    }

    private JwtProperties jwtProperties(String secretKey) {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecretKey(secretKey);
        jwtProperties.setAccessTokenValiditySeconds(8640000);
        jwtProperties.setRefreshTokenValiditySeconds(8640000);
        return jwtProperties;
    }
}
