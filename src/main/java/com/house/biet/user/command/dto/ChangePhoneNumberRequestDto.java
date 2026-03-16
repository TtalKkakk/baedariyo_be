package com.house.biet.user.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePhoneNumberRequestDto(
        @NotBlank
        @Size(min = 11, max = 13)
        String phoneNumber
) {
}
