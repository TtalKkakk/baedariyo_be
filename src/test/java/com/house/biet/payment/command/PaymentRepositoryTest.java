package com.house.biet.payment.command;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.repository.PaymentRepositoryJpaAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(PaymentRepositoryJpaAdapter.class)
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("Payment 저장 및 ID 조회 테스트")
    void save_Success_And_FindById() {
        // given
        Payment payment = Payment.create(
                1L,
                3L,
                new Money(10000),
                new PaymentKey("pk_test_123")
        );

        // when
        Payment saved = paymentRepository.save(payment);
        Optional<Payment> found = paymentRepository.findById(saved.getId());

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(found).isPresent();
        assertThat(found.get().getOrderId()).isEqualTo(1L);
        assertThat(found.get().getStatus()).isEqualTo(PaymentStatus.READY);
    }

    @Test
    @DisplayName("paymentKey 기준 조회 테스트")
    void findByPaymentKey_Success() {
        // given
        Long orderId = 2L;
        Long userId = 3L;
        String key = "pk_test_456";

        Payment payment = Payment.create(
                orderId,
                userId,
                new Money(20000),
                new PaymentKey(key)
        );

        paymentRepository.save(payment);

        // when
        Optional<Payment> found = paymentRepository.findByPaymentKey(key);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getOrderId()).isEqualTo(orderId);
        assertThat(found.get().getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("paymentKey 중복 저장 시 예외 발생")
    void duplicate_Error_PaymentKey_Should_Throw_Exception() {
        // given
        String key = "pk_test_duplicate";

        Payment payment1 = Payment.create(
                3L,
                4L,
                new Money(30000),
                new PaymentKey(key)
        );

        Payment payment2 = Payment.create(
                4L,
                7L,
                new Money(40000),
                new PaymentKey(key)
        );

        paymentRepository.save(payment1);

        // when & then
        assertThatThrownBy(() -> paymentRepository.save(payment2))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("existsByPaymentKey 테스트")
    void existsByPaymentKey_Success() {
        // given
        String key = "pk_test_exists";

        Payment payment = Payment.create(
                5L,
                12L,
                new Money(50000),
                new PaymentKey(key)
        );

        paymentRepository.save(payment);

        // when
        boolean exists = paymentRepository.existsByPaymentKey(key);

        // then
        assertThat(exists).isTrue();
    }
}