package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuNameTest {

    @Test
    @DisplayName("성공 - 정상적인 메뉴 이름 생성")
    void createMenuName_Success() {
        // given
        String givenMenuName = "고추마요순살";

        // when
        MenuName menuName = new MenuName(givenMenuName);

        // then
        assertThat(menuName).isNotNull();
        assertThat(menuName.value()).isEqualTo(givenMenuName);
    }

    @Test
    @DisplayName("성공 - 50자 메뉴 이름 생성")
    void create50MenuName_Success() {
        // given
        String fiftyMenuName = "a".repeat(50);

        // when
        MenuName menuName = new MenuName(fiftyMenuName);

        // then
        assertThat(menuName).isNotNull();
        assertThat(menuName.value()).isEqualTo(fiftyMenuName);
    }

    @Test
    @DisplayName("실패 - 메뉴 이름이 null이면 예외 발생")
    void createMenuName_Fail_NullValue() {
        // given
        String nullValue = null;

        // when & then
        assertThatThrownBy(() -> new MenuName(nullValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - 메뉴 이름이 빈 문자열이면 예외 발생")
    void createMenuName_Fail_BlankValue() {
        // given
        String blankValue = "   ";

        // when & then
        assertThatThrownBy(() -> new MenuName(blankValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }


    @Test
    @DisplayName("실패 - 메뉴 이름 길이가 50자를 초과하면 예외 발생")
    void createMenuName_Fail_TooLongValue() {
        // given
        String longMenuName = "a".repeat(51);

        // when & then
        assertThatThrownBy(() -> new MenuName(longMenuName))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MENU_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("성공 - 같은 값의 MenuName은 동등하다")
    void equalsAndHashCode_SameValue() {
        // given
        MenuName menuName1 = new MenuName("고추마요순살");
        MenuName menuName2 = new MenuName("고추마요순살");

        // then
        assertThat(menuName1).isEqualTo(menuName2);
        assertThat(menuName1.hashCode()).isEqualTo(menuName2.hashCode());
    }

    @Test
    @DisplayName("성공 - 다른 값의 MenuName은 동등하지 않다")
    void equalsAndHashCode_DifferentValue() {
        // given
        MenuName menuName1 = new MenuName("고추마요순살");
        MenuName menuName2 = new MenuName("간장마늘치킨");

        // then
        assertThat(menuName1).isNotEqualTo(menuName2);
    }
}
