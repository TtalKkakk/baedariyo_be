package com.house.biet.order.query.repository.dto;

public record OrderSummaryQueryDto(
        Long orderId,
        String storeName,
        String storeAddress,
        String customerAddress,
        int fee,
        double storeLatitude,
        double storeLongitude,
        double customerLatitude,
        double customerLongitude
) {
}
