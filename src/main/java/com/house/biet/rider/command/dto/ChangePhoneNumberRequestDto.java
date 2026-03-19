package com.house.biet.rider.command.dto;

import jakarta.validation.constraints.NotNull;

public record ChangePhoneNumberRequestDto(

        @NotNull(message = "전화번호는 필수입니다.")
        String phoneNumber

) {}