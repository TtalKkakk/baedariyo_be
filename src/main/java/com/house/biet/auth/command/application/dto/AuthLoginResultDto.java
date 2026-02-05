package com.house.biet.auth.command.application.dto;

public record AuthLoginResultDto(
        Long accountId,
        String accessToken,
        String refreshToken
) {}
