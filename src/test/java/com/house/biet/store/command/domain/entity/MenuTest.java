package com.house.biet.store.command.domain.entity;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.MenuFixture;
import com.house.biet.fixtures.MenuOptionGroupFixture;
import com.house.biet.fixtures.StoreFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.MenuName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    @DisplayName("성공 - 메뉴 생성")
    void CreateMenu_Success() {
        // given
        Store store = StoreFixture.createStore();
        MenuName menuName = new MenuName("불고기버거");
        Money price = new Money(8000);
        String description = "대표 메뉴";

        // when
        Menu menu = Menu.create(store, menuName, price, description);

        // then
        assertThat(menu.getStore()).isEqualTo(store);
        assertThat(menu.getMenuName()).isEqualTo(menuName);
        assertThat(menu.getPrice()).isEqualTo(price);
        assertThat(menu.getMenuDescription()).isEqualTo(description);
        assertThat(menu.getMenuOptionGroups()).isEmpty();
    }

    @Test
    @DisplayName("에러 - Store가 null이면 메뉴 생성 실패")
    void CreateMenu_Fail_StoreIsNull() {
        // given
        Store store = null;
        MenuName menuName = new MenuName("불고기버거");
        Money price = new Money(8000);

        // when & then
        assertThatThrownBy(() ->
                Menu.create(store, menuName, price, null)
        )
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_ID.getMessage());
    }

    @Test
    @DisplayName("에러 - 메뉴 이름이 null이면 메뉴 생성 실패")
    void CreateMenu_Fail_MenuNameIsNull() {
        // given
        Store store = StoreFixture.createStore();
        MenuName menuName = null;
        Money price = new Money(8000);

        // when & then
        assertThatThrownBy(() ->
                Menu.create(store, menuName, price, null)
        )
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 가격이 null이면 메뉴 생성 실패")
    void CreateMenu_Fail_PriceIsNull() {
        // given
        Store store = StoreFixture.createStore();
        MenuName menuName = new MenuName("불고기버거");
        Money price = null;

        // when & then
        assertThatThrownBy(() ->
                Menu.create(store, menuName, price, null)
        )
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_PRICE.getMessage());
    }

    @Test
    @DisplayName("성공 - 메뉴에 옵션 그룹 추가")
    void AddOptionGroup_Success() {
        // given
        Store store = StoreFixture.createStore();
        Menu menu = MenuFixture.aMenu()
                .withStore(store)
                .build();

        MenuOptionGroup optionGroup =
                MenuOptionGroupFixture.aMenuOptionGroup()
                        .withGroupName("추가 토핑")
                        .withMaxSelectableCount(2)
                        .build();

        // when
        menu.addOptionGroup(optionGroup);

        // then
        assertThat(menu.getMenuOptionGroups()).hasSize(1);
        assertThat(menu.getMenuOptionGroups().get(0)).isEqualTo(optionGroup);
        assertThat(optionGroup.getMenu()).isEqualTo(menu);
    }
}