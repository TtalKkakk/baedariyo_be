package com.house.biet.user.command.dto;

import com.house.biet.user.command.domain.entity.UserPaymentMethod;

public record PaymentMethodResponseDto(Long id, String cardName, String cardNumber, boolean isDefault) {

    public static PaymentMethodResponseDto from(UserPaymentMethod pm) {
        return new PaymentMethodResponseDto(pm.getId(), pm.getCardName(), pm.getCardNumber(), pm.isDefault());
    }
}
