package com.house.biet.user.command.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RemoveAddressRequestDto(

        @NotNull(message = "주소 ID는 필수입니다.")
        @Positive(message = "주소 ID는 0보다 커야 합니다.")
        Long addressId
) {}