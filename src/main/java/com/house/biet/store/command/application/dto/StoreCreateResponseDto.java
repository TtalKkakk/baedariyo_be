package com.house.biet.store.command.application.dto;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.store.command.domain.aggregate.Store;

import java.util.UUID;

public record StoreCreateResponseDto(
        UUID storePublicId,
        String storeName,
        StoreCategory storeCategory
) {

    public static StoreCreateResponseDto fromEntity(Store store) {
        return new StoreCreateResponseDto(
                store.getPublicId(),
                store.getStoreName().getValue(),
                store.getStoreCategory()
        );
    }
}