package com.house.biet.store.query.dto;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.entity.Menu;

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
            String menuName,
            Money price,
            String menuDescription
    ) {
        public static MenuDto fromEntity(Menu menu) {
            return new MenuDto(
                    menu.getMenuName().getValue(),
                    menu.getPrice(),
                    menu.getMenuDescription()
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