package com.house.biet.store.command.application.dto;

import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;
import jakarta.validation.constraints.*;

public record StoreReviewCreateRequestDto(
        @NotNull
        Long orderId,

        @Min(1) @Max(5)
        int rating,

        StoreReviewImages storeReviewImages,

        StoreReviewComment storeReviewComment
) {}