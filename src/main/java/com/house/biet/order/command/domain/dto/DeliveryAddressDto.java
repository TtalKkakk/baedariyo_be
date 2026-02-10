package com.house.biet.order.command.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record DeliveryAddressDto(

        @NotBlank
        String roadAddress,

        @NotBlank
        String jibunAddress,

        @NotBlank
        String detailAddress
) {
}