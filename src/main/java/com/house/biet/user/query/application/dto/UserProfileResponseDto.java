package com.house.biet.user.query.application.dto;

public record UserProfileResponseDto(
        String nickname,
        String phoneNumber,
        String email
) {
}
