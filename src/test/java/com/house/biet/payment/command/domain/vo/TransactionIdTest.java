package com.house.biet.payment.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TransactionIdTest {

    @Test
    @DisplayName("성공 - 유효한 TransactionId 생성")
    void constructor_Success_ValidValue() {

        // given
        String validId = "TXN-123456789";

        // when
        TransactionId transactionId = new TransactionId(validId);

        // then
        assertThat(transactionId.getValue()).isEqualTo(validId);
    }

    @Test
    @DisplayName("실패 - null 값으로 생성 시 INVALID_TRANSACTION_ID 예외 발생")
    void constructor_Error_NullValue() {

        // given
        String invalidId = null;

        // when & then
        assertThatThrownBy(() -> new TransactionId(invalidId))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TRANSACTION_ID);
    }

    @Test
    @DisplayName("실패 - blank 값으로 생성 시 INVALID_TRANSACTION_ID 예외 발생")
    void constructor_Error_BlankValue() {

        // given
        String invalidId = "   ";

        // when & then
        assertThatThrownBy(() -> new TransactionId(invalidId))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TRANSACTION_ID);
    }

    @Test
    @DisplayName("성공 - 동일한 값이면 동등한 객체로 판단된다")
    void equals_Success_SameValue() {

        // given
        TransactionId id1 = new TransactionId("TXN-111");
        TransactionId id2 = new TransactionId("TXN-111");

        // when & then
        assertThat(id1)
                .isEqualTo(id2)
                .hasSameHashCodeAs(id2);
    }

    @Test
    @DisplayName("성공 - 값이 다르면 다른 객체로 판단된다")
    void equals_Success_DifferentValue() {

        // given
        TransactionId id1 = new TransactionId("TXN-111");
        TransactionId id2 = new TransactionId("TXN-222");

        // when & then
        assertThat(id1).isNotEqualTo(id2);
    }
}