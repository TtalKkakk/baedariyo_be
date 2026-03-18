package com.house.biet.user.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class AddressAliasTest {

    @Test
    @DisplayName("성공 - 정상적인 값으로 addressAlias 생성")
    void createAddressAlias_Success() {
        // given
        String value = "집";

        // when
        AddressAlias alias = new AddressAlias(value);

        // then
        assertThat(alias.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("성공 - 길이 10자까지 허용")
    void createAddressAlias_Success_MaxLength() {
        // given
        String value = "1234567890"; // 10자

        // when
        AddressAlias alias = new AddressAlias(value);

        // then
        assertThat(alias.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("실패 - null 입력 시 예외 발생")
    void createAddressAlias_Error_Null() {
        // when & then
        assertThatThrownBy(() -> new AddressAlias(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - 공백 문자열 입력 시 예외 발생")
    void createAddressAlias_Error_Blank() {
        // given
        String value = "   ";

        // when & then
        assertThatThrownBy(() -> new AddressAlias(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - 길이 초과 시 예외 발생")
    void createAddressAlias_Error_LengthExceeded() {
        // given
        String value = "1".repeat(51); // 51자

        // when & then
        assertThatThrownBy(() -> new AddressAlias(value))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("성공 - toString은 value를 반환")
    void toString_Success() {
        // given
        String value = "회사";

        // when
        AddressAlias alias = new AddressAlias(value);

        // then
        assertThat(alias.toString()).isEqualTo(value);
    }
}