package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    String addressValue;

    @Test
    @DisplayName("성공 - Address 생성")
    void createAddress_Success() {
        // given
        addressValue = "서울시 마포구";

        // when
        Address address = new Address(addressValue);

        // then
        assertThat(address).isNotNull();
        assertThat(address.getFullAddressValue()).isEqualTo(addressValue);
    }

    @Test
    @DisplayName("에러 - addressValue가 null")
    void createAddress_Error_NullAddressValue() {
        // given
        addressValue = null;

        // when & then
        assertThatThrownBy(() -> new Address(addressValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - addressValue 가 비어있음")
    void createAddress_Error_EmptyAddressValue() {
        // given
        addressValue = "";

        // when & then
        assertThatThrownBy(() -> new Address(addressValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_FORMAT.getMessage());
    }
}