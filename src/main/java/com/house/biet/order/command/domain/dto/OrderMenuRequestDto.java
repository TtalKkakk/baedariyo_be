package com.house.biet.order.command.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderMenuRequestDto(

        @NotNull
        Long menuId,

        @NotBlank
        String menuName,

        @Min(0)
        int menuPrice,

        @Min(1)
        int quantity
) {
}
