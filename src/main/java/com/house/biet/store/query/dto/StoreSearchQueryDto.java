package com.house.biet.store.query.dto;

import java.util.UUID;

public record StoreSearchQueryDto(

        UUID storePublicId,
        String storeThumbnail,
        String storeName,
        double storeLatitude,
        double storeLongitude,
        int minOrderPrice,
        double rating,
        int reviewCount,
        double distance
) { }