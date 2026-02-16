package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreNameTest {

    @Test
    @DisplayName("성공 - StoreName 생성")
    void CreateStoreName_Success() {
        // given
        String givenValue = "a".repeat(50);

        // when
        StoreName storeName = new StoreName(givenValue);

        // then
        assertThat(storeName.getValue()).isEqualTo(givenValue);
    }

    @Test
    @DisplayName("에러 - 가게 이름이 null")
    void CreateStoreName_ValueIsNull() {
        // given
        String givenValue = null;

        // when & then
        assertThatThrownBy(() -> new StoreName(givenValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 가게 이름이 빈 문자열")
    void CreateStoreName_ValueIsEmpty() {
        // given
        String givenValue = "";

        // when & then
        assertThatThrownBy(() -> new StoreName(givenValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 가게 이름이 공백 문자열")
    void CreateStoreName_ValueIsBlank() {
        // given
        String givenValue = "   ";

        // when & then
        assertThatThrownBy(() -> new StoreName(givenValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_NAME_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - 가게 이름이 50자 초과")
    void CreateStoreName_ValueTooLong() {
        // given
        String givenValue = "a".repeat(51);

        // when & then
        assertThatThrownBy(() -> new StoreName(givenValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_NAME_FORMAT.getMessage());
    }
}
