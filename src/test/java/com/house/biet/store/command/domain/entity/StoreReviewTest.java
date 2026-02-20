package com.house.biet.store.command.domain.entity;

import com.house.biet.store.command.domain.vo.StoreReviewComment;
import com.house.biet.store.command.domain.vo.StoreReviewImages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StoreReviewTest {

    @Test
    @DisplayName("성공 - StoreReview 생성")
    void createStoreReview_Success_AllFields() {
        // given
        UUID storeId = UUID.randomUUID();
        Long userId = 100L;
        Long orderId = 2L;
        int rating = 5;
        StoreReviewImages storeReviewImages = new StoreReviewImages(List.of("img1", "img2"));
        StoreReviewComment comment = new StoreReviewComment("맛있어요");

        // when
        StoreReview storeReview = StoreReview.create(
                storeId,
                userId,
                orderId,
                rating,
                storeReviewImages,
                comment
        );

        // then
        assertThat(storeReview.getStorePublicId()).isEqualTo(storeId);
        assertThat(storeReview.getUserId()).isEqualTo(userId);
        assertThat(storeReview.getOrderId()).isEqualTo(orderId);
        assertThat(storeReview.getRating()).isEqualTo(rating);
        assertThat(storeReview.getStoreReviewImages()).isEqualTo(storeReviewImages);
        assertThat(storeReview.getStoreReviewComment()).isEqualTo(comment);
    }

    @Test
    @DisplayName("성공 - comment 없이 StoreReview 생성")
    void createStoreReview_Success_WithoutComment() {
        // given
        UUID storeId = UUID.randomUUID();

        // when
        StoreReview storeReview = StoreReview.create(
                storeId,
                100L,
                2L,
                4,
                new StoreReviewImages(null),
                null
        );

        // then
        assertThat(storeReview.getStoreReviewComment()).isNull();
    }

    @Test
    @DisplayName("성공 - rating 값 보존")
    void createStoreReview_Success_RatingPreserved() {
        // given
        int rating = 3;

        // when
        StoreReview storeReview = StoreReview.create(
                UUID.randomUUID(),
                100L,
                2L,
                rating,
                new StoreReviewImages(null),
                null
        );

        // then
        assertThat(storeReview.getRating()).isEqualTo(rating);
    }

    @Test
    @DisplayName("성공 - embedded comment 값 보존")
    void createStoreReview_Success_EmbeddedCommentPreserved() {
        // given
        StoreReviewComment comment = new StoreReviewComment("괜찮아요");

        // when
        StoreReview storeReview = StoreReview.create(
                UUID.randomUUID(),
                100L,
                2L,
                5,
                new StoreReviewImages(null),
                comment
        );

        // then
        assertThat(storeReview.getStoreReviewComment()).isNotNull();
        assertThat(storeReview.getStoreReviewComment().getComment()).isEqualTo("괜찮아요");
    }
}
