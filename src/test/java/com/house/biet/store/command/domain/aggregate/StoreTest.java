package com.house.biet.store.command.domain.aggregate;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.StoreOperationInfoFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.command.domain.vo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StoreTest {

    /* =========================
       createStore
       ========================= */

    @Test
    @DisplayName("성공 - 가게 생성")
    void createStore_Success() {
        // given
        StoreName storeName = new StoreName("테스트 가게");
        StoreCategory category = StoreCategory.CAFE_DESSERT;

        // when
        Store store = Store.create(
                storeName,
                category,
                null,
                null,
                StoreOperationInfoFixture.aStoreOperationInfo().build(),
                new Money(15000),
                new Money(3000)
        );

        // then
        assertThat(store.getPublicId()).isNotNull();
        assertThat(store.getStoreName()).isEqualTo(storeName);
        assertThat(store.getStoreCategory()).isEqualTo(category);
        assertThat(store.getRating().getReviewCount()).isZero();
    }

    /* =========================
       addMenu
       ========================= */

    @Test
    @DisplayName("성공 - 메뉴 추가")
    void addMenu_Success() {
        // given
        Store store = createStore();

        // when
        Menu menu = store.addMenu(
                new MenuName("아메리카노"),
                new Money(4500),
                "진한 커피"
        );

        // then
        assertThat(store.getMenus()).hasSize(1);
        assertThat(menu.getStore()).isEqualTo(store);
    }

    @Test
    @DisplayName("실패 - 메뉴 가격이 null 이면 예외 발생")
    void addMenu_Error_InvalidMenuPrice() {
        // given
        Store store = createStore();

        // when & then
        assertThatThrownBy(() ->
                store.addMenu(
                        new MenuName("아메리카노"),
                        null,
                        "진한 커피"
                )
        )
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_MENU_PRICE);
    }

    /* =========================
       addRating
       ========================= */

    @Test
    @DisplayName("성공 - 별점 추가 시 누적된다")
    void addRating_Success() {
        // given
        Store store = createStore();

        // when
        store.addRating(5);
        store.addRating(4);

        // then
        assertThat(store.getRating().getReviewCount()).isEqualTo(2);
        assertThat(store.getRating().getTotalRating()).isEqualTo(9);
    }

    @Test
    @DisplayName("성공 - 별점 제거 시 누적 값이 감소한다")
    void removeRating_Success() {
        // given
        Store store = createStore();
        store.addRating(5);
        store.addRating(4); // total 9, count 2

        // when
        store.removeRating(4);

        // then
        assertThat(store.getRating().getReviewCount()).isEqualTo(1);
        assertThat(store.getRating().getTotalRating()).isEqualTo(5);
        assertThat(store.getRating().average()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("성공 - 마지막 별점 제거 시 0으로 초기화")
    void removeRating_Success_LastReview() {
        // given
        Store store = createStore();
        store.addRating(5);

        // when
        store.removeRating(5);

        // then
        assertThat(store.getRating().getReviewCount()).isZero();
        assertThat(store.getRating().getTotalRating()).isZero();
        assertThat(store.getRating().average()).isZero();
    }

    @Test
    @DisplayName("실패 - 리뷰가 없는데 제거하면 예외 발생")
    void removeRating_Error_NoReview() {
        // given
        Store store = createStore();

        // when & then
        assertThatThrownBy(() -> store.removeRating(5))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_RATING_SCORE.getMessage());
    }

    /* =========================
       테스트 헬퍼
       ========================= */

    private Store createStore() {
        return Store.create(
                new StoreName("테스트 가게"),
                StoreCategory.CAFE_DESSERT,
                null,
                null,
                StoreOperationInfoFixture.aStoreOperationInfo().build(),
                new Money(10000),
                new Money(2000)
        );
    }
}
