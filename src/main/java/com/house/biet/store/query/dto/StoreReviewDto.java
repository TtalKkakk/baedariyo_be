package com.house.biet.store.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class StoreReviewDto {

    private UUID publicStoreReviewId;
    private Long userId;
    private String storeName;
    private int rating;
    private LocalDateTime createdAt;
    private List<String> orderMenuImages;
    private String storeReviewComment;
}
