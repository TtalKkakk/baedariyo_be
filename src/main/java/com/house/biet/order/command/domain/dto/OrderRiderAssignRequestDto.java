package com.house.biet.order.command.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRiderAssignRequestDto(
        @NotBlank
        Long orderId
) {
}
