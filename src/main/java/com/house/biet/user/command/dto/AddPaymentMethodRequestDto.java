package com.house.biet.user.command.dto;

public record AddPaymentMethodRequestDto(String billingKey, String cardNumber) {
}
