package com.house.biet.store.command.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.house.biet.fixtures.MenuFixture;
import com.house.biet.fixtures.MenuOptionGroupFixture;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.entity.MenuOptionGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuTest {

    @Test
    @DisplayName("성공 - 메뉴에 옵션 그룹 추가")
    void AddOptionGroup_Success() {
        // given
        Store store = new Store(); // store 도 fixture 있으면 대체 가능
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