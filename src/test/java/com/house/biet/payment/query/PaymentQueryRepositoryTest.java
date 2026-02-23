package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.command.repository.PaymentRepositoryJpaAdapter;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryJpaAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        PaymentRepositoryJpaAdapter.class,
        PaymentQueryRepositoryJpaAdapter.class
})
class PaymentQueryRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentQueryRepository paymentQueryRepository;

    @Test
    @DisplayName("특정 상태의 결제 목록 조회")
    void findAllByStatus() {
        // given
        Payment ready = Payment.create(
                1L,
                3L,
                new Money(10000),
                new PaymentKey("pk_ready")
        );

        Payment approved = Payment.create(
                2L,
                4L,
                new Money(20000),
                new PaymentKey("pk_approved")
        );
        approved.request();
        approved.approve(new TransactionId("tx_123"));

        paymentRepository.save(ready);
        paymentRepository.save(approved);

        // when
        List<Payment> readyPayments =
                paymentQueryRepository.findAllByStatus(PaymentStatus.READY);

        // then
        assertThat(readyPayments).hasSize(1);
        assertThat(readyPayments.get(0).getOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 주문의 결제 이력 조회")
    void findAllByOrderId() {
        // given
        Long orderId = 10L;
        Long userId = 7L;

        Payment payment1 = Payment.create(
                orderId,
                userId,
                new Money(15000),
                new PaymentKey("pk_order_1")
        );

        Payment payment2 = Payment.create(
                orderId,
                userId,
                new Money(25000),
                new PaymentKey("pk_order_2")
        );

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        // when
        List<Payment> payments =
                paymentQueryRepository.findAllByOrderId(orderId);

        // then
        assertThat(payments).hasSize(2);
        assertThat(payments)
                .extracting(Payment::getOrderId)
                .containsOnly(orderId);
    }
}