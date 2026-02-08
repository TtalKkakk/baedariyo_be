package com.house.biet.order.command.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderMenuRequestDto(

        @NotNull
        Long menuId,

        @Min(1)
        int quantity
) {
}
