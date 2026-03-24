package com.house.biet.store.query.dto;

public record StoreMenuQueryDto(
        Long menuId,
        String menuName,
        int price,
        String menuDescription
) {
}
