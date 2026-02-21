package com.house.biet.store.command.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.StoreOperationInfoFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.StoreName;
import com.house.biet.support.config.ServiceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StoreRatingServiceIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRatingService storeRatingService;

    private UUID storePublicId;

    @BeforeEach
    void setup() {

        // given
        Store store = Store.create(
                new StoreName("평점 테스트 가게"),
                StoreCategory.CAFE_DESSERT,
                null,
                null,
                StoreOperationInfoFixture.aStoreOperationInfo().build(),
                new Money(10000),
                new Money(2000)
        );

        Store saved = storeService.save(store);
        storePublicId = saved.getPublicId();
    }

    /* =========================
       increaseRating
       ========================= */

    @Test
    @DisplayName("성공 - 평점 증가 시 DB에 반영된다")
    void increaseRating_Success() {

        // when
        storeRatingService.increaseRating(storePublicId, 5);
        storeRatingService.increaseRating(storePublicId, 4);

        // then
        Store store = storeService.getStoreByPublicId(storePublicId);

        assertThat(store.getRating().getReviewCount()).isEqualTo(2);
        assertThat(store.getRating().getTotalRating()).isEqualTo(9);
        assertThat(store.getRating().average()).isEqualTo(4.5);
    }

    /* =========================
       decreaseRating
       ========================= */

    @Test
    @DisplayName("성공 - 평점 감소 시 DB에 반영된다")
    void decreaseRating_Success() {

        // given
        storeRatingService.increaseRating(storePublicId, 5);
        storeRatingService.increaseRating(storePublicId, 3);

        // when
        storeRatingService.decreaseRating(storePublicId, 3);

        // then
        Store store = storeService.getStoreByPublicId(storePublicId);

        assertThat(store.getRating().getReviewCount()).isEqualTo(1);
        assertThat(store.getRating().getTotalRating()).isEqualTo(5);
        assertThat(store.getRating().average()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 가게")
    void increaseRating_Error_StoreNotFound() {

        // given
        UUID invalidId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> storeRatingService.increaseRating(invalidId, 5))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STORE_NOT_FOUND.getMessage());
    }
}