package com.house.biet.store.query.dto;

public record StoreSearchResponseDto(
        String storeName,
        int deliveryTime,
        double deliveryDistance,
        int minOrderPrice,
        double rating,
        int reviewCount
) {
}
