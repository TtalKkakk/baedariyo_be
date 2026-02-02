package com.house.biet.auth.infrastructure.jwt;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthPrincipal principal;

    public JwtAuthenticationToken(AuthPrincipal principal) {
        super(Collections.emptyList());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return principal;
    }
}
