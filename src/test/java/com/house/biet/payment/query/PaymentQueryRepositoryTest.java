package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.command.repository.PaymentRepositoryJpaAdapter;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryJpa;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryJpaAdapter;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryQuerydsl;
import com.house.biet.support.config.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
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

    @Autowired
    private PaymentQueryRepositoryJpa paymentQueryRepositoryJpa;

    @Test
    @DisplayName("성공 - 상태로 결제 목록을 조회한다")
    void findAllByStatus_Success() {
        // given
        Payment ready = Payment.create(1L, 3L, new Money(10000), new PaymentKey("pk_ready"));
        Payment approved = Payment.create(2L, 4L, new Money(20000), new PaymentKey("pk_approved"));
        approved.request();
        approved.approve(new TransactionId("tx_123"));
        paymentRepository.save(ready);
        paymentRepository.save(approved);

        // when
        List<PaymentDetailResponseDto> readyPayments = paymentQueryRepository.findAllByStatus(PaymentStatus.READY);

        // then
        assertThat(readyPayments).hasSize(1);
        assertThat(readyPayments.get(0).orderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("성공 - 결제 ID로 결제 상세를 조회한다")
    void findById_Success() {
        // given
        Payment payment = Payment.create(1L, 3L, new Money(10000), new PaymentKey("pk_ready"));
        Payment savedPayment = paymentRepository.save(payment);

        // when
        PaymentDetailResponseDto foundPayment = paymentQueryRepository.findById(savedPayment.getId()).orElse(null);

        // then
        assertThat(foundPayment).isNotNull();
        assertThat(foundPayment.paymentId()).isEqualTo(savedPayment.getId());
    }

    @Test
    @DisplayName("성공 - 주문 ID로 결제 목록을 조회한다")
    void findAllByOrderId_Success() {
        // given
        Long orderId = 10L;
        Long userId = 7L;
        paymentRepository.save(Payment.create(orderId, userId, new Money(15000), new PaymentKey("pk_order_1")));
        paymentRepository.save(Payment.create(orderId, userId, new Money(25000), new PaymentKey("pk_order_2")));

        // when
        List<PaymentDetailResponseDto> payments = paymentQueryRepository.findAllByOrderId(orderId);

        // then
        assertThat(payments).hasSize(2);
        assertThat(payments).extracting(PaymentDetailResponseDto::orderId).containsOnly(orderId);
    }

    @Test
    @DisplayName("성공 - 사용자 ID로 결제 목록을 조회한다")
    void findAllByUserId_Success() {
        // given
        Long userId = 100L;
        paymentRepository.save(Payment.create(1L, userId, new Money(10000), new PaymentKey("pk_user_all_1")));
        paymentRepository.save(Payment.create(2L, userId, new Money(20000), new PaymentKey("pk_user_all_2")));
        paymentRepository.save(Payment.create(3L, 999L, new Money(30000), new PaymentKey("pk_user_all_other")));

        // when
        List<PaymentDetailResponseDto> result = paymentQueryRepository.findAllByUserId(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(PaymentDetailResponseDto::userId).containsOnly(userId);
    }

    @Test
    @DisplayName("성공 - 사용자 ID와 상태 목록으로 결제 목록을 조회한다")
    void findAllByUserIdAndStatusIn_Success() {
        // given
        Long userId = 200L;
        Payment ready = Payment.create(10L, userId, new Money(10000), new PaymentKey("pk_filter_ready"));
        Payment approved = Payment.create(11L, userId, new Money(20000), new PaymentKey("pk_filter_approved"));
        approved.request();
        approved.approve(new TransactionId("tx_filter_1"));
        Payment failed = Payment.create(12L, userId, new Money(30000), new PaymentKey("pk_filter_failed"));
        failed.request();
        failed.fail();
        paymentRepository.save(ready);
        paymentRepository.save(approved);
        paymentRepository.save(failed);

        // when
        List<PaymentDetailResponseDto> result = paymentQueryRepository.findAllByUserIdAndStatusIn(userId, List.of(PaymentStatus.APPROVED, PaymentStatus.CANCELED));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("성공 - 주문 ID로 승인된 결제를 조회한다")
    void findApprovedByOrderId_Success() {
        // given
        Long orderId = 300L;
        Payment payment = Payment.create(orderId, 55L, new Money(50000), new PaymentKey("pk_approved_find"));
        payment.request();
        payment.approve(new TransactionId("tx_find_approved"));
        paymentRepository.save(payment);

        // when
        var result = paymentQueryRepository.findApprovedByOrderId(orderId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("성공 - paymentKey로 결제를 조회한다")
    void findByPaymentKey_Success() {
        // given
        Payment payment = Payment.create(400L, 77L, new Money(70000), new PaymentKey("pk_unique_test"));
        paymentRepository.save(payment);

        // when
        var result = paymentQueryRepository.findByPaymentKey("pk_unique_test");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().orderId()).isEqualTo(400L);
    }

    @Test
    @DisplayName("실패 - 승인된 결제가 없는 주문을 조회할 때 빈 결과를 반환한다")
    void findApprovedByOrderId_Error_ApprovedPaymentNotFound() {
        // given
        Long orderId = 9999L;
        Payment payment = Payment.create(orderId, 1L, new Money(10000), new PaymentKey("pk_no_approve"));
        paymentRepository.save(payment);

        // when
        var result = paymentQueryRepository.findApprovedByOrderId(orderId);

        // then
        assertThat(result).isEmpty();
    }
}