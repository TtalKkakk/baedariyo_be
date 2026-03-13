package com.house.biet.store.query.dto;

import com.querydsl.core.annotations.QueryProjection;

public record StoreSearchQueryDto(

        String storeName,
        double storeLatitude,
        double storeLongitude,
        int minOrderPrice,
        double rating,
        int reviewCount,
        double distance
) {

    @QueryProjection
    public StoreSearchQueryDto {}
}