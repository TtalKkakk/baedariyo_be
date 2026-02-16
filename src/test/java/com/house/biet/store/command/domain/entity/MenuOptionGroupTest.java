package com.house.biet.store.command.domain.entity;

import com.house.biet.fixtures.MenuOptionGroupFixture;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuOptionGroupTest {

    @Test
    @DisplayName("성공 - 선택한 옵션 개수가 최대 선택 가능 개수 이하")
    void ValidateSelectableCount_Success() {
        // given
        MenuOptionGroup group = new MenuOptionGroupFixture()
                .withMaxSelectableCount(3)
                .build();

        int selectedCount = 2;

        // when & then
        group.validateSelectableCount(selectedCount);
    }

    @Test
    @DisplayName("에러 - 선택한 옵션 개수가 최대 선택 가능 개수 초과")
    void ValidateSelectableCount_Error_ExceedMaxCount() {
        // given
        MenuOptionGroup group = new MenuOptionGroupFixture()
                .withMaxSelectableCount(2)
                .build();

        int selectedCount = 3;

        // when & then
        assertThatThrownBy(() -> group.validateSelectableCount(selectedCount))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.EXCEED_MAX_OPTION_SELECTABLE_COUNT.getMessage());
    }
}
