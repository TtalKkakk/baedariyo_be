package com.house.biet.payment.query;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.command.repository.PaymentRepositoryJpaAdapter;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryJpaAdapter;
import com.house.biet.payment.query.repository.PaymentQueryRepositoryJpa;
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
    @DisplayName("finds payment details by status")
    void findAllByStatus() {
        Payment ready = Payment.create(1L, 3L, new Money(10000), new PaymentKey("pk_ready"));
        Payment approved = Payment.create(2L, 4L, new Money(20000), new PaymentKey("pk_approved"));
        approved.request();
        approved.approve(new TransactionId("tx_123"));

        paymentRepository.save(ready);
        paymentRepository.save(approved);

        List<PaymentDetailResponseDto> readyPayments = paymentQueryRepository.findAllByStatus(PaymentStatus.READY);

        assertThat(readyPayments).hasSize(1);
        assertThat(readyPayments.get(0).orderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("finds payment detail by id")
    void findById_Success() {
        Payment payment = Payment.create(1L, 3L, new Money(10000), new PaymentKey("pk_ready"));
        Payment savedPayment = paymentRepository.save(payment);

        PaymentDetailResponseDto foundPayment = paymentQueryRepository.findById(savedPayment.getId()).orElse(null);

        assertThat(foundPayment).isNotNull();
        assertThat(foundPayment.paymentId()).isEqualTo(savedPayment.getId());
    }

    @Test
    @DisplayName("finds payment details by order id")
    void findAllByOrderId() {
        Long orderId = 10L;
        Long userId = 7L;

        paymentRepository.save(Payment.create(orderId, userId, new Money(15000), new PaymentKey("pk_order_1")));
        paymentRepository.save(Payment.create(orderId, userId, new Money(25000), new PaymentKey("pk_order_2")));

        List<PaymentDetailResponseDto> payments = paymentQueryRepository.findAllByOrderId(orderId);

        assertThat(payments).hasSize(2);
        assertThat(payments).extracting(PaymentDetailResponseDto::orderId).containsOnly(orderId);
    }

    @Test
    @DisplayName("finds payment details by user id")
    void findAllByUserId_Success() {
        Long userId = 100L;

        paymentRepository.save(Payment.create(1L, userId, new Money(10000), new PaymentKey("pk_user_all_1")));
        paymentRepository.save(Payment.create(2L, userId, new Money(20000), new PaymentKey("pk_user_all_2")));
        paymentRepository.save(Payment.create(3L, 999L, new Money(30000), new PaymentKey("pk_user_all_other")));

        List<PaymentDetailResponseDto> result = paymentQueryRepository.findAllByUserId(userId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(PaymentDetailResponseDto::userId).containsOnly(userId);
    }

    @Test
    @DisplayName("finds payment details by user id and statuses")
    void findAllByUserIdAndStatusIn_Success() {
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

        List<PaymentDetailResponseDto> result =
                paymentQueryRepository.findAllByUserIdAndStatusIn(userId, List.of(PaymentStatus.APPROVED, PaymentStatus.CANCELED));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("finds approved payment detail by order id")
    void findApprovedByOrderId_Success() {
        Long orderId = 300L;
        Payment payment = Payment.create(orderId, 55L, new Money(50000), new PaymentKey("pk_approved_find"));
        payment.request();
        payment.approve(new TransactionId("tx_find_approved"));
        paymentRepository.save(payment);

        var result = paymentQueryRepository.findApprovedByOrderId(orderId);

        assertThat(result).isPresent();
        assertThat(result.get().status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("finds payment detail by payment key")
    void findByPaymentKey_Success() {
        Payment payment = Payment.create(400L, 77L, new Money(70000), new PaymentKey("pk_unique_test"));
        paymentRepository.save(payment);

        var result = paymentQueryRepository.findByPaymentKey("pk_unique_test");

        assertThat(result).isPresent();
        assertThat(result.get().orderId()).isEqualTo(400L);
    }

    @Test
    @DisplayName("returns empty when approved payment is absent")
    void findApprovedByOrderId_Error_NotFound() {
        Long orderId = 9999L;
        Payment payment = Payment.create(orderId, 1L, new Money(10000), new PaymentKey("pk_no_approve"));
        paymentRepository.save(payment);

        var result = paymentQueryRepository.findApprovedByOrderId(orderId);

        assertThat(result).isEmpty();
    }
}
