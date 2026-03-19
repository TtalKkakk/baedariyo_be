package com.house.biet.rider.command.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeNicknameRequestDto(

        @NotNull(message = "닉네임은 필수입니다.")
        String nickname

) {}