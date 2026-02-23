package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.command.domain.vo.TransactionId;
import com.house.biet.payment.query.PaymentQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PaymentQueryServiceTest {

    @Mock
    private PaymentQueryRepository paymentQueryRepository;

    @InjectMocks
    private PaymentQueryServiceImpl paymentQueryService;

    @Test
    @DisplayName("상태 기준 조회")
    void findByStatus() {
        Payment payment = Payment.create(
                1L, 2L,
                new Money(10000),
                new PaymentKey("pk_status")
        );

        given(paymentQueryRepository.findAllByStatus(PaymentStatus.READY))
                .willReturn(List.of(payment));

        List<Payment> result =
                paymentQueryService.findByStatus(PaymentStatus.READY);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 기준 조회")
    void findByOrderId() {
        Payment payment = Payment.create(
                2L, 3L,
                new Money(1000),
                new PaymentKey("pk_o1")
        );

        given(paymentQueryRepository.findAllByOrderId(2L))
                .willReturn(List.of(payment));

        List<Payment> result =
                paymentQueryService.findByOrderId(2L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("사용자 기준 조회")
    void findByUserId() {
        Payment payment = Payment.create(
                3L, 10L,
                new Money(5000),
                new PaymentKey("pk_user")
        );

        given(paymentQueryRepository.findAllByUserId(10L))
                .willReturn(List.of(payment));

        List<Payment> result =
                paymentQueryService.findByUserId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("사용자 + 상태 목록 조회")
    void findByUserIdAndStatuses() {
        Payment payment = Payment.create(
                4L, 20L,
                new Money(8000),
                new PaymentKey("pk_multi")
        );

        given(paymentQueryRepository
                .findAllByUserIdAndStatusIn(20L, List.of(PaymentStatus.READY)))
                .willReturn(List.of(payment));

        List<Payment> result =
                paymentQueryService.findByUserIdAndStatuses(
                        20L,
                        List.of(PaymentStatus.READY)
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("주문 승인 결제 조회 - 존재하는 경우")
    void findApprovedByOrderId_success() {
        Payment payment = Payment.create(
                5L, 30L,
                new Money(12000),
                new PaymentKey("pk_approved")
        );
        payment.request();
        payment.approve(new TransactionId("tx_123"));

        given(paymentQueryRepository.findApprovedByOrderId(5L))
                .willReturn(Optional.of(payment));

        Optional<Payment> result =
                paymentQueryService.findApprovedByOrderId(5L);

        assertThat(result).isPresent();
        assertThat(result.get().getOrderId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("주문 승인 결제 조회 - 없는 경우")
    void findApprovedByOrderId_empty() {
        given(paymentQueryRepository.findApprovedByOrderId(99L))
                .willReturn(Optional.empty());

        Optional<Payment> result =
                paymentQueryService.findApprovedByOrderId(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("paymentKey 기준 조회 - 존재하는 경우")
    void findByPaymentKey_success() {
        Payment payment = Payment.create(
                6L, 40L,
                new Money(7000),
                new PaymentKey("pk_find")
        );

        given(paymentQueryRepository.findByPaymentKey("pk_find"))
                .willReturn(Optional.of(payment));

        Optional<Payment> result =
                paymentQueryService.findByPaymentKey("pk_find");

        assertThat(result).isPresent();
        assertThat(result.get().getPaymentKey().getValue())
                .isEqualTo("pk_find");
    }

    @Test
    @DisplayName("paymentKey 기준 조회 - 없는 경우")
    void findByPaymentKey_empty() {
        given(paymentQueryRepository.findByPaymentKey("not_exist"))
                .willReturn(Optional.empty());

        Optional<Payment> result =
                paymentQueryService.findByPaymentKey("not_exist");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("조회 결과 없을 경우 빈 리스트 반환")
    void findByUserId_emptyList() {
        given(paymentQueryRepository.findAllByUserId(123L))
                .willReturn(List.of());

        List<Payment> result =
                paymentQueryService.findByUserId(123L);

        assertThat(result).isEmpty();
    }
}