package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.command.repository.PaymentRepositoryJpaAdapter;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryJpaAdapter;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryQuerydsl;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.support.config.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        PaymentRepositoryJpaAdapter.class,
        PaymentQueryRepositoryJpaAdapter.class,
        PaymentQueryRepositoryQuerydsl.class,
        QueryDslTestConfig.class
})
@ActiveProfiles("test")
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
    @DisplayName("id를 이용하여 특정 주문 조회")
    void findById_Success() {
        Payment payment = Payment.create(
                1L,
                3L,
                new Money(10000),
                new PaymentKey("pk_ready")
        );

        Payment savedPayment = paymentRepository.save(payment);
        Long paymentId = savedPayment.getId();

        // when
        Payment foundPayment = paymentQueryRepository.findById(paymentId).orElse(null);

        // then
        assertThat(foundPayment).isNotNull();
        assertThat(foundPayment.getId()).isEqualTo(paymentId);

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

    @Test
    @DisplayName("성공 - 특정 사용자의 전체 결제 이력 조회")
    void findAllByUserId_Success() {
        // given
        Long userId = 100L;

        Payment payment1 = Payment.create(
                1L, userId,
                new Money(10000),
                new PaymentKey("pk_user_all_1")
        );

        Payment payment2 = Payment.create(
                2L, userId,
                new Money(20000),
                new PaymentKey("pk_user_all_2")
        );

        Payment otherUserPayment = Payment.create(
                3L, 999L,
                new Money(30000),
                new PaymentKey("pk_user_all_other")
        );

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(otherUserPayment);

        // when
        List<Payment> result =
                paymentQueryRepository.findAllByUserId(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Payment::getUserId)
                .containsOnly(userId);
    }


    @Test
    @DisplayName("성공 - 특정 사용자의 승인/취소 결제만 조회")
    void findAllByUserIdAndStatusIn_Success() {
        // given
        Long userId = 200L;

        Payment ready = Payment.create(
                10L, userId,
                new Money(10000),
                new PaymentKey("pk_filter_ready")
        );

        Payment approved = Payment.create(
                11L, userId,
                new Money(20000),
                new PaymentKey("pk_filter_approved")
        );
        approved.request();
        approved.approve(new TransactionId("tx_filter_1"));

        Payment failed = Payment.create(
                12L, userId,
                new Money(30000),
                new PaymentKey("pk_filter_failed")
        );
        failed.request();
        failed.fail();

        paymentRepository.save(ready);
        paymentRepository.save(approved);
        paymentRepository.save(failed);

        // when
        List<Payment> result =
                paymentQueryRepository.findAllByUserIdAndStatusIn(
                        userId,
                        List.of(PaymentStatus.APPROVED, PaymentStatus.CANCELED)
                );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus())
                .isEqualTo(PaymentStatus.APPROVED);
    }


    @Test
    @DisplayName("성공 - 특정 주문의 승인된 결제 조회")
    void findApprovedByOrderId_Success() {
        // given
        Long orderId = 300L;
        Long userId = 55L;

        Payment payment = Payment.create(
                orderId,
                userId,
                new Money(50000),
                new PaymentKey("pk_approved_find")
        );

        payment.request();
        payment.approve(new TransactionId("tx_find_approved"));

        paymentRepository.save(payment);

        // when
        var result =
                paymentQueryRepository.findApprovedByOrderId(orderId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getStatus())
                .isEqualTo(PaymentStatus.APPROVED);
    }


    @Test
    @DisplayName("성공 - paymentKey 기준 결제 조회")
    void findByPaymentKey_Success() {
        // given
        Payment payment = Payment.create(
                400L,
                77L,
                new Money(70000),
                new PaymentKey("pk_unique_test")
        );

        paymentRepository.save(payment);

        // when
        var result =
                paymentQueryRepository.findByPaymentKey("pk_unique_test");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getOrderId()).isEqualTo(400L);
    }


    @Test
    @DisplayName("실패 - 승인된 결제가 존재하지 않으면 빈 Optional 반환")
    void findApprovedByOrderId_Error_NotFound() {
        // given
        Long orderId = 9999L;

        Payment payment = Payment.create(
                orderId,
                1L,
                new Money(10000),
                new PaymentKey("pk_no_approve")
        );

        paymentRepository.save(payment);

        // when
        var result =
                paymentQueryRepository.findApprovedByOrderId(orderId);

        // then
        assertThat(result).isEmpty();
    }

}