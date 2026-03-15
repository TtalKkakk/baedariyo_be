package com.house.biet.storeSearch.query.event.dto;

public record StoreSearchEvent(
        Long userId,
        String keyword
) {
}
