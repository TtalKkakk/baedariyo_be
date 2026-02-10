package com.house.biet.order.command.domain.dto;

import com.house.biet.order.command.domain.vo.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequestDto(

        @NotNull
        Long storeId,

        @NotEmpty
        List<OrderMenuRequestDto> menus,

        String storeRequest,

        String riderRequest,

        @NotBlank
        DeliveryAddressDto deliveryAddress,

        @NotNull
        PaymentMethod paymentMethod
) {
}
