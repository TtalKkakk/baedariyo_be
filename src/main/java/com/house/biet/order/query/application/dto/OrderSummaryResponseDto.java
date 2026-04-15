package com.house.biet.order.query.application.dto;

public record OrderSummaryResponseDto(
        Long orderId,
        String storeName,
        String storeAddress,
        String customerAddress,
        int fee,
        String distance,
        int estimatedMinutes
) {
}
