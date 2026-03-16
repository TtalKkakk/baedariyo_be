package com.house.biet.user.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeNicknameRequestDto(
        @NotBlank
        @Size(min = 5, max = 30)
        String nickname
) {
}
