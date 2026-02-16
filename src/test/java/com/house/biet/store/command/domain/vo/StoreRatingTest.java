package com.house.biet.store.command.domain.vo;

import com.house.biet.fixtures.StoreRatingFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreRatingTest {

    @Test
    @DisplayName("성공 - 초기 StoreRating 은 totalRating 과 reviewCount 가 0이다")
    void empty_rating() {
        // when
        StoreRating rating = StoreRating.empty();

        // then
        assertThat(rating.getTotalRating()).isZero();
        assertThat(rating.getReviewCount()).isZero();
        assertThat(rating.average()).isZero();
    }

    @Test
    @DisplayName("성공 - 별점을 하나 추가하면 total 과 count 가 증가한다")
    void add_single_rating() {
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
    @DisplayName("성공 - 여러 개의 별점을 추가하면 평균이 정확히 계산된다")
    void calculate_average() {
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
    @DisplayName("성공 - 리뷰가 없으면 평균은 0.0이다")
    void average_when_no_review() {
        // given
        StoreRating rating = StoreRatingFixture.empty();

        // when
        double average = rating.average();

        // then
        assertThat(average).isZero();
    }
}
