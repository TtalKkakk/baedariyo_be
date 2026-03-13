package com.house.biet.store.query.dto;

import com.house.biet.common.domain.enums.StoreCategory;

public record StoreSearchRequestDto(
        String keyword,
        StoreCategory storeCategory,
        Double latitude,
        Double longitude,
        Integer page,
        Integer size
) {
    public int getPageOrDefault() {
        return page == null ? 0 : page;
    }

    public int getSizeOrDefault() {
        return size == null ? 10 : size;
    }
}
