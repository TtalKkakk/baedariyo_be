package com.house.biet.auth.infrastructure.security;

import com.house.biet.global.vo.UserRole;

public record AuthPrincipal(
        Long accountId,
        UserRole role
) {
}
