package com.house.biet.auth.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank
        @Size(min = 15, max = 40)
        String password
) {
}
