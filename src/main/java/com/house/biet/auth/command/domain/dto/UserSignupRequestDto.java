package com.house.biet.auth.command.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserSignupRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name,

        @NotBlank
        String nickname,

        @NotBlank
        String phoneNumber
) {
}
