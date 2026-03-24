package com.house.biet.store.query.dto;

import java.util.List;
import java.util.UUID;

public record StoreDetailWithMenuAndReviewResponseDto(
        UUID storePublicId,
        String storeName,
        String thumbnailUrl,
        int totalRating,
        int reviewCount,
        List<MenuDto> menus,
        List<StoreReviewPhotoDto> recentPhotoReviews
) {

    public record MenuDto(
            Long menuId,
            String menuName,
            int price,
            String menuDescription
    ) {
        public static MenuDto from(StoreMenuQueryDto menu) {
            return new MenuDto(
                    menu.menuId(),
                    menu.menuName(),
                    menu.price(),
                    menu.menuDescription()
            );
        }
    }

    public record StoreReviewPhotoDto(
            String storeReviewComment,
            int rating,
            String thumbnailImages
    ) {
        public static StoreReviewPhotoDto fromDtoEntity(StoreReviewDto reviewDto) {
            return new StoreReviewPhotoDto(
                    reviewDto.getStoreReviewComment(),
                    reviewDto.getRating(),
                    reviewDto.getStoreReviewImages().get(0)
            );
        }
    }
}
