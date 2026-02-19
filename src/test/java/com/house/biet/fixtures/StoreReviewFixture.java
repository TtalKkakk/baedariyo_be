package com.house.biet.fixtures;

import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class StoreReviewFixture {

    public static StoreReview create() {
        return new StoreReview(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                100L,
                2L,
                5,
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
                new StoreReviewComment("맛있어요")
        );
    }

    public static StoreReview createWithRating(int rating) {
        return new StoreReview(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                100L,
                2L,
                rating,
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
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
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
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
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
                new StoreReviewComment("괜찮아요")
        );
    }

    public static StoreReview createWithStoreId(UUID storePublicId) {
        return new StoreReview(
                null,
                UUID.randomUUID(),
                storePublicId,
                100L,
                2L,
                5,
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
                new StoreReviewComment("괜찮아요")
        );
    }

    public static StoreReview createWithUserId(Long userId) {
        return new StoreReview(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                userId,
                2L,
                4,
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
                null
        );
    }

    public static StoreReview createWithStoreIdAndUserId(UUID storePublicId, long userId) {
        return new StoreReview(
                null,
                UUID.randomUUID(),
                storePublicId,
                userId,
                2L,
                5,
                new StoreReviewImages(List.of("img" + new Random().nextInt())),
                new StoreReviewComment("괜찮아요")
        );
    }
}
