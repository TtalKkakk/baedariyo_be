package com.house.biet.auth.infrastructure.security;

import com.house.biet.common.domain.enums.UserRole;

public record AuthPrincipal(
        Long accountId,
        UserRole role
) {
}
