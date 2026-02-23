package com.house.biet.payment.command.domain.aggregate;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    // ===== Fixture =====

    Payment payment;
    TransactionId transactionId;

    @BeforeEach
    void setup() {
        payment = Payment.create(
                1L,
                new Money(10000),
                new PaymentKey("PAY-KEY-1")
        );

        transactionId = new TransactionId("TXN-1");
    }

    // ===== 생성 =====

    @Test
    @DisplayName("성공 - Payment 생성 시 READY 상태로 초기화된다")
    void create_Success_InitializeReadyStatus() {
        // given
        Long orderId = 1L;
        Money money = new Money(10000);
        PaymentKey key = new PaymentKey("PAY-KEY-1");

        // when
        Payment payment = Payment.create(orderId, money, key);

        // then
        assertThat(payment.getOrderId()).isEqualTo(orderId);
        assertThat(payment.getMoney()).isEqualTo(money);
        assertThat(payment.getPaymentKey()).isEqualTo(key);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.READY);
        assertThat(payment.getTransactionId()).isNull();
    }

    // ===== 상태 전이 =====

    @Test
    @DisplayName("성공 - READY 상태에서 REQUESTED로 전이된다")
    void request_Success() {
        // when
        payment.request();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REQUESTED);
    }

    @Test
    @DisplayName("성공 - REQUESTED 상태에서 APPROVED로 전이되고 transactionId가 설정된다")
    void approve_Success() {
        // given
        payment.request();

        // when
        payment.approve(transactionId);

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(payment.getTransactionId()).isEqualTo(transactionId);
        assertThat(payment.isApproved()).isTrue();
    }

    @Test
    @DisplayName("성공 - REQUESTED 상태에서 FAILED로 전이된다")
    void fail_Success() {
        // given
        payment.request();

        // when
        payment.fail();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("성공 - APPROVED 상태에서 CANCELED로 전이된다")
    void cancel_Success() {
        // given
        payment.request();
        payment.approve(transactionId);

        // when
        payment.cancel();

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
    }

    // ===== 잘못된 전이 =====

    @Test
    @DisplayName("실패 - READY 상태에서 바로 APPROVED 전이 시 예외 발생")
    void approve_Error_InvalidTransition() {
        // when & then
        assertThatThrownBy(() -> payment.approve(transactionId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION.getMessage());
    }

    @Test
    @DisplayName("실패 - READY 상태에서 CANCEL 전이 시 예외 발생")
    void cancel_Error_InvalidTransition() {
        // when & then
        assertThatThrownBy(payment::cancel)
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION.getMessage());
    }
}