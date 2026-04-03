package com.house.biet.store.query.dto;

import java.util.UUID;

public record StoreSearchResponseDto(

        UUID storePublicId,
        String thumbnailUrl,
        String storeName,
        int deliveryTime,
        double deliveryDistance,
        int minOrderPrice,
        double rating,
        int reviewCount
) {
}
