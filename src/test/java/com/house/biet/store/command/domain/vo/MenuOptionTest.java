package com.house.biet.store.command.domain.vo;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuOptionTest {

    @Test
    @DisplayName("성공 - MenuOption 생성")
    void CreateMenuOption_Success() {
        // given
        String givenName = "옵션1";
        Money givenOptionPrice = new Money(1000);

        // when
        MenuOption menuOption = new MenuOption(givenName, givenOptionPrice);

        // then
        assertThat(menuOption.getName()).isEqualTo(givenName);
        assertThat(menuOption.getOptionPrice()).isEqualTo(givenOptionPrice);
    }

    @Test
    @DisplayName("에러 - 옵션 이름이 null")
    void CreateMenuOption_NameIsNull() {
        // given
        String givenName = null;
        Money givenOptionPrice = new Money(1000);

        // when & then
        assertThatThrownBy(() -> new MenuOption(givenName, givenOptionPrice))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_OPTION_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 옵션 이름이 빈 문자열")
    void CreateMenuOption_NameIsEmpty() {
        // given
        String givenName = "";
        Money givenOptionPrice = new Money(1000);

        // when & then
        assertThatThrownBy(() -> new MenuOption(givenName, givenOptionPrice))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_OPTION_NAME_FORMAT.getMessage());
    }
}