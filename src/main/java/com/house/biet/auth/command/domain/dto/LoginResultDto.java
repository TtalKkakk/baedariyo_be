package com.house.biet.auth.command.domain.dto;

public record LoginResultDto(
        String accessToken,
        String refreshToken
) {
}
