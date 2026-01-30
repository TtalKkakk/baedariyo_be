package com.house.biet.auth.command.domain.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginResultDto {

    private final String accessToken;
    private final String refreshToken;
}
