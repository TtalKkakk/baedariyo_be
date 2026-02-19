package com.house.biet.store.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class MyStoreReviewDto {

    private UUID publicStoreReviewId;
    private String storeName;
    private int rating;
    private LocalDateTime createdAt;
    private String thumbnailImage;
    private String storeReviewComment;
}
