package com.house.biet.store.command.domain.vo;

import com.house.biet.fixtures.StoreRatingFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StoreRatingTest {

    @Test
    @DisplayName("성공 - 초기 StoreRating 생성")
    void createStoreRating_Success() {
        // when
        StoreRating rating = StoreRating.empty();

        // then
        assertThat(rating.getTotalRating()).isZero();
        assertThat(rating.getReviewCount()).isZero();
        assertThat(rating.average()).isZero();
    }

    @Test
    @DisplayName("성공 - 별점 하나 추가")
    void addRating_Success() {
        // given
        StoreRating rating = StoreRating.empty();

        // when
        StoreRating updated = rating.addRating(5);

        // then
        assertThat(updated.getTotalRating()).isEqualTo(5);
        assertThat(updated.getReviewCount()).isEqualTo(1);
        assertThat(updated.average()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("성공 - 여러 별점 평균 계산")
    void calculateAverage_Success() {
        // given
        StoreRating rating = StoreRatingFixture.ratedBy(5, 4, 3);

        // when
        double average = rating.average();

        // then
        assertThat(rating.getTotalRating()).isEqualTo(12);
        assertThat(rating.getReviewCount()).isEqualTo(3);
        assertThat(average).isEqualTo(4.0);
    }

    @Test
    @DisplayName("성공 - 리뷰가 없으면 평균은 0")
    void calculateAverage_Success_NoReview() {
        // given
        StoreRating rating = StoreRatingFixture.empty();

        // when
        double average = rating.average();

        // then
        assertThat(average).isZero();
    }

    @Test
    @DisplayName("실패 - 별점이 1~5 범위를 벗어나면 예외 발생")
    void addRating_Error_InvalidRatingScore() {
        // given
        StoreRating rating1 = StoreRating.empty();
        int invalidScore1 = 0;
        int invalidScore2 = 6;

        // when & then
        assertThatThrownBy(() -> rating1.addRating(invalidScore1))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_RATING_SCORE.getMessage());

        assertThatThrownBy(() -> rating1.addRating(invalidScore2))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_RATING_SCORE.getMessage());
    }
}
