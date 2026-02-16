package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuNameTest {

    @Test
    @DisplayName("성공 - 메뉴 이름 생성")
    void createMenuName_success() {
        // given
        String givenName = "불고기버거";

        // when
        MenuName menuName = new MenuName(givenName);

        // then
        assertThat(menuName.getValue()).isEqualTo(givenName);
    }

    @Test
    @DisplayName("에러 - 메뉴 이름이 null")
    void createMenuName_nameIsNull() {
        // given
        String givenName = null;

        // when & then
        assertThatThrownBy(() -> new MenuName(givenName))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 메뉴 이름이 빈 문자열")
    void createMenuName_nameIsBlank() {
        // given
        String givenName = "   ";

        // when & then
        assertThatThrownBy(() -> new MenuName(givenName))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 메뉴 이름 길이 초과")
    void createMenuName_lengthExceeded() {
        // given
        String givenName = "a".repeat(51);

        // when & then
        assertThatThrownBy(() -> new MenuName(givenName))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("동등성 - 같은 값이면 같은 MenuName")
    void equalsAndHashCode_sameValue() {
        // given
        MenuName menuName1 = new MenuName("치킨");
        MenuName menuName2 = new MenuName("치킨");

        // then
        assertThat(menuName1).isEqualTo(menuName2);
        assertThat(menuName1.hashCode()).isEqualTo(menuName2.hashCode());
    }
}
