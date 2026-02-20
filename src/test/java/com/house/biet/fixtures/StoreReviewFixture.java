package com.house.biet.fixtures;

import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class StoreReviewFixture {

    private static final Random RANDOM = new Random();

    public static StoreReview create() {
        return StoreReview.create(
                UUID.randomUUID(),
                100L, // userId
                2L,   // orderId
                5,    // rating
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                new StoreReviewComment("맛있어요")
        );
    }

    public static StoreReview createWithRating(int rating) {
        return StoreReview.create(
                UUID.randomUUID(),
                100L,
                2L,
                rating,
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                new StoreReviewComment("괜찮아요")
        );
    }

    public static StoreReview createWithoutComment() {
        return StoreReview.create(
                UUID.randomUUID(),
                100L,
                2L,
                4,
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                null
        );
    }

    public static StoreReview createWithPublicId(UUID publicReviewId) {
        StoreReview review = StoreReview.create(
                UUID.randomUUID(),
                100L,
                2L,
                5,
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                new StoreReviewComment("괜찮아요")
        );

        try {
            Field field = StoreReview.class.getDeclaredField("publicStoreReviewId");
            field.setAccessible(true);
            field.set(review, publicReviewId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return review;
    }

    public static StoreReview createWithStoreId(UUID storePublicId) {
        return StoreReview.create(
                storePublicId,
                100L,
                2L,
                5,
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                new StoreReviewComment("괜찮아요")
        );
    }

    public static StoreReview createWithUserId(Long userId) {
        return StoreReview.create(
                UUID.randomUUID(),
                userId,
                2L,
                4,
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                null
        );
    }

    public static StoreReview createWithStoreIdAndUserId(UUID storePublicId, long userId) {
        return StoreReview.create(
                storePublicId,
                userId,
                2L,
                5,
                new StoreReviewImages(List.of("img" + RANDOM.nextInt())),
                new StoreReviewComment("괜찮아요")
        );
    }
}