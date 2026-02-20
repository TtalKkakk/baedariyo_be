package com.house.biet.store.command.application.dto;

import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.domain.vo.StoreReviewComment;

import java.util.UUID;

public record StoreReviewCreateResponseDto(
        UUID storeReviewPublicId,
        UUID storePublicId,
        int rating,
        StoreReviewComment storeReviewComment
) {
    public static StoreReviewCreateResponseDto fromEntity(StoreReview review) {
        return new StoreReviewCreateResponseDto(
                review.getPublicId(),
                review.getStorePublicId(),
                review.getRating(),
                review.getStoreReviewComment()
        );
    }
}