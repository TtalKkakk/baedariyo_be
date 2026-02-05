package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    @DisplayName("성공 - Money 생성")
    void createMoney_Success() {
        // given
        int givenAmount = 1000;

        // when
        Money money = new Money(givenAmount);

        // then
        assertThat(money.value()).isEqualTo(givenAmount);
    }

    @Test
    @DisplayName("에러 - 음수 amount")
    void createMoney_Error_NegativeAmount() {
        // given
        int givenNegativeAmount = -1000;

        // when & then
        assertThatThrownBy(() -> new Money(givenNegativeAmount))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_MONEY_AMOUNT.getMessage());
    }
}