package com.house.biet.payment.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentKeyTest {

    @Test
    @DisplayName("성공 - 유효한 PaymentKey 생성")
    void constructor_Success_ValidValue() {

        // given
        String validKey = "PAY-KEY-123456";

        // when
        PaymentKey paymentKey = new PaymentKey(validKey);

        // then
        assertThat(paymentKey.getValue()).isEqualTo(validKey);
    }

    @Test
    @DisplayName("실패 - null 값으로 생성 시 INVALID_PAYMENT_KEY 예외 발생")
    void constructor_Error_NullValue() {

        // given
        String invalidKey = null;

        // when & then
        assertThatThrownBy(() -> new PaymentKey(invalidKey))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PAYMENT_KEY);
    }

    @Test
    @DisplayName("실패 - blank 값으로 생성 시 INVALID_PAYMENT_KEY 예외 발생")
    void constructor_Error_BlankValue() {

        // given
        String invalidKey = "   ";

        // when & then
        assertThatThrownBy(() -> new PaymentKey(invalidKey))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_PAYMENT_KEY);
    }

    @Test
    @DisplayName("성공 - 동일한 값이면 동등한 객체로 판단된다")
    void equals_Success_SameValue() {

        // given
        PaymentKey key1 = new PaymentKey("PAY-123");
        PaymentKey key2 = new PaymentKey("PAY-123");

        // when & then
        assertThat(key1)
                .isEqualTo(key2)
                .hasSameHashCodeAs(key2);
    }

    @Test
    @DisplayName("성공 - 값이 다르면 다른 객체로 판단된다")
    void equals_Success_DifferentValue() {

        // given
        PaymentKey key1 = new PaymentKey("PAY-123");
        PaymentKey key2 = new PaymentKey("PAY-999");

        // when & then
        assertThat(key1).isNotEqualTo(key2);
    }
}