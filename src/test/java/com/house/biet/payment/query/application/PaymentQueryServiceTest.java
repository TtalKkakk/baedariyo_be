package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import com.house.biet.payment.query.PaymentQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.List;

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
        // given
        Payment payment = Payment.create(
                1L,
                new Money(10000),
                new PaymentKey("pk_status")
        );

        given(paymentQueryRepository.findAllByStatus(PaymentStatus.READY))
                .willReturn(List.of(payment));

        // when
        List<Payment> result =
                paymentQueryService.findByStatus(PaymentStatus.READY);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 기준 조회")
    void findByOrderId() {
        // given
        Payment payment1 = Payment.create(
                2L,
                new Money(1000),
                new PaymentKey("pk_o1")
        );

        given(paymentQueryRepository.findAllByOrderId(2L))
                .willReturn(List.of(payment1));

        // when
        List<Payment> result =
                paymentQueryService.findByOrderId(2L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(2L);
    }
}