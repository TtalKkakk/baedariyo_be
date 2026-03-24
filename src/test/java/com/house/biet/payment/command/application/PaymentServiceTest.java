package com.house.biet.payment.command.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.payment.command.PaymentRepository;
import com.house.biet.payment.command.application.event.PaymentApprovedEvent;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.command.domain.vo.PaymentKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("성공 - 유효한 값으로 결제를 생성한다")
    void createPayment_Success() {
        // given
        given(paymentRepository.existsByPaymentKey("pk_1")).willReturn(false);
        Payment savedPayment = Payment.create(1L, 2L, new Money(10000), new PaymentKey("pk_1"));
        Long paymentId = 999L;
        ReflectionTestUtils.setField(savedPayment, "id", paymentId);
        given(paymentRepository.save(any(Payment.class))).willReturn(savedPayment);

        // when
        Long id = paymentService.createPayment(1L, 2L, new Money(10000), "pk_1");

        // then
        assertThat(id).isNotNull().isEqualTo(paymentId);
        verify(paymentRepository).existsByPaymentKey("pk_1");
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("실패 - 중복 paymentKey로 결제를 생성할 때 에러")
    void createPayment_Error_DuplicatePayment() {
        // given
        given(paymentRepository.existsByPaymentKey("dup")).willReturn(true);

        // when & then
        assertThatThrownBy(() -> paymentService.createPayment(1L, 2L, new Money(1000), "dup"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATE_PAYMENT.getMessage());
    }

    @Test
    @DisplayName("성공 - 요청된 결제를 승인한다")
    void approvePayment_Success() {
        // given
        Payment payment = Payment.create(1L, 2L, new Money(10000), new PaymentKey("pk_approve"));
        payment.request();
        given(paymentRepository.findById(1L)).willReturn(Optional.of(payment));

        // when
        paymentService.approve(1L, "tx_123");

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(payment.getTransactionId()).isNotNull();
        verify(eventPublisher).publishEvent(any(PaymentApprovedEvent.class));
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 결제를 승인할 때 에러")
    void approvePayment_Error_PaymentNotFound() {
        // given
        given(paymentRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> paymentService.approve(999L, "tx"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PAYMENT_NOT_FOUND.getMessage());
    }
}