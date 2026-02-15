package com.house.biet.common.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("성공 - Money 덧셈")
    void addMoney_Success() {
        // given
        int givenAmount1 = 1100;
        int givenAmount2 = 1300;

        Money money1 = new Money(givenAmount1);
        Money money2 = new Money(givenAmount2);

        // when
        Money totalMoney = money1.add(money2);

        // then
        assertThat(totalMoney).isNotNull();
        assertThat(totalMoney).isNotEqualTo(money1);
        assertThat(totalMoney.value()).isEqualTo(givenAmount1 + givenAmount2);
    }

    @Test
    @DisplayName("성공 - Money 곱셈")
    void multiplyMoney_Success() {
        // given
        int givenAmount = 1000;
        Money money = new Money(givenAmount);

        int givenQuantity = 5;

        // when
        Money multiMoney = money.multiply(givenQuantity);

        // then
        assertThat(multiMoney).isNotNull();
        assertThat(multiMoney).isNotEqualTo(money);
        assertThat(multiMoney.value()).isEqualTo(givenAmount * givenQuantity);
    }
}