package com.house.biet.fixtures;

import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;

import java.util.UUID;

public class StoreReviewFixture {

    public static StoreReview create() {
        return new StoreReview(
                1L,
                UUID.randomUUID(),
                UUID.randomUUID(),
                100L,
                2L,
                5,
                new StoreReviewImages(null),
                new StoreReviewComment("맛있어요")
        );
    }

    public static StoreReview createWithRating(int rating) {
        return new StoreReview(
                1L,
                UUID.randomUUID(),
                UUID.randomUUID(),
                100L,
                2L,
                rating,
                new StoreReviewImages(null),
                new StoreReviewComment("괜찮아요")
        );
    }

    public static StoreReview createWithoutComment() {
        return new StoreReview(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                100L,
                2L,
                4,
                new StoreReviewImages(null),
                null
        );
    }

    public static StoreReview createWithPublicId(UUID publicReviewId) {
        return new StoreReview(
                null,
                publicReviewId,
                UUID.randomUUID(),
                100L,
                2L,
                5,
                new StoreReviewImages(null),
                new StoreReviewComment("괜찮아요")
        );
    }
}
