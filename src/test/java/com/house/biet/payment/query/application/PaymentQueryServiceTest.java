package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.query.PaymentQueryRepository;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    @DisplayName("finds payments by status")
    void findByStatus() {
        PaymentDetailResponseDto dto = paymentDetail(1L, 1L, 2L, PaymentStatus.READY, "pk_status");
        given(paymentQueryRepository.findAllByStatus(PaymentStatus.READY)).willReturn(List.of(dto));

        List<PaymentDetailResponseDto> result = paymentQueryService.findByStatus(PaymentStatus.READY);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).orderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("finds payment detail by id")
    void findById_Success() {
        PaymentDetailResponseDto dto = paymentDetail(2L, 2L, 3L, PaymentStatus.READY, "pk_o1");
        given(paymentQueryRepository.findById(2L)).willReturn(Optional.of(dto));

        Optional<PaymentDetailResponseDto> result = paymentQueryService.findById(2L);

        assertThat(result).contains(dto);
    }

    @Test
    @DisplayName("finds payment details by order id")
    void findByOrderId() {
        PaymentDetailResponseDto dto = paymentDetail(3L, 2L, 3L, PaymentStatus.READY, "pk_o1");
        given(paymentQueryRepository.findAllByOrderId(2L)).willReturn(List.of(dto));

        List<PaymentDetailResponseDto> result = paymentQueryService.findByOrderId(2L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).orderId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("finds payment details by user id")
    void findByUserId() {
        PaymentDetailResponseDto dto = paymentDetail(4L, 3L, 10L, PaymentStatus.READY, "pk_user");
        given(paymentQueryRepository.findAllByUserId(10L)).willReturn(List.of(dto));

        List<PaymentDetailResponseDto> result = paymentQueryService.findByUserId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).userId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("finds payment details by user id and statuses")
    void findByUserIdAndStatuses() {
        PaymentDetailResponseDto dto = paymentDetail(5L, 4L, 20L, PaymentStatus.READY, "pk_multi");
        given(paymentQueryRepository.findAllByUserIdAndStatusIn(20L, List.of(PaymentStatus.READY)))
                .willReturn(List.of(dto));

        List<PaymentDetailResponseDto> result =
                paymentQueryService.findByUserIdAndStatuses(20L, List.of(PaymentStatus.READY));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).userId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("finds approved payment by order id")
    void findApprovedByOrderId_success() {
        PaymentDetailResponseDto dto = paymentDetail(6L, 5L, 30L, PaymentStatus.APPROVED, "pk_approved");
        given(paymentQueryRepository.findApprovedByOrderId(5L)).willReturn(Optional.of(dto));

        Optional<PaymentDetailResponseDto> result = paymentQueryService.findApprovedByOrderId(5L);

        assertThat(result).contains(dto);
        assertThat(result.get().status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("returns empty when approved payment is absent")
    void findApprovedByOrderId_empty() {
        given(paymentQueryRepository.findApprovedByOrderId(99L)).willReturn(Optional.empty());

        Optional<PaymentDetailResponseDto> result = paymentQueryService.findApprovedByOrderId(99L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("finds payment by payment key")
    void findByPaymentKey_success() {
        PaymentDetailResponseDto dto = paymentDetail(7L, 6L, 40L, PaymentStatus.READY, "pk_find");
        given(paymentQueryRepository.findByPaymentKey("pk_find")).willReturn(Optional.of(dto));

        Optional<PaymentDetailResponseDto> result = paymentQueryService.findByPaymentKey("pk_find");

        assertThat(result).contains(dto);
        assertThat(result.get().paymentKey()).isEqualTo("pk_find");
    }

    @Test
    @DisplayName("returns empty when payment key is absent")
    void findByPaymentKey_empty() {
        given(paymentQueryRepository.findByPaymentKey("not_exist")).willReturn(Optional.empty());

        Optional<PaymentDetailResponseDto> result = paymentQueryService.findByPaymentKey("not_exist");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("returns empty list when user has no payments")
    void findByUserId_emptyList() {
        given(paymentQueryRepository.findAllByUserId(123L)).willReturn(List.of());

        List<PaymentDetailResponseDto> result = paymentQueryService.findByUserId(123L);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("finds my payment detail list without status filter")
    void findMyPaymentDetailList_Success_allStatus() {
        Long userId = 1L;
        MyPaymentSearchCondition condition = new MyPaymentSearchCondition(userId, null);
        MyPaymentDetailResponseDto dto = new MyPaymentDetailResponseDto(
                "store1",
                PaymentStatus.READY,
                null,
                List.of(),
                null,
                List.of(),
                10000,
                null
        );

        given(paymentQueryRepository.findMyPaymentDetailList(condition)).willReturn(List.of(dto));

        List<MyPaymentDetailResponseDto> result = paymentQueryService.findMyPaymentDetailList(condition);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).storeName()).isEqualTo("store1");
    }

    @Test
    @DisplayName("finds my payment detail list with status filter")
    void findMyPaymentDetailList_Success_withStatus() {
        Long userId = 2L;
        MyPaymentSearchCondition condition = new MyPaymentSearchCondition(userId, PaymentStatus.APPROVED);
        MyPaymentDetailResponseDto dto = new MyPaymentDetailResponseDto(
                "store2",
                PaymentStatus.APPROVED,
                5,
                List.of(),
                "good",
                List.of("img1"),
                20000,
                null
        );

        given(paymentQueryRepository.findMyPaymentDetailList(condition)).willReturn(List.of(dto));

        List<MyPaymentDetailResponseDto> result = paymentQueryService.findMyPaymentDetailList(condition);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).paymentStatus()).isEqualTo(PaymentStatus.APPROVED);
    }

    private PaymentDetailResponseDto paymentDetail(
            Long paymentId,
            Long orderId,
            Long userId,
            PaymentStatus status,
            String paymentKey
    ) {
        return new PaymentDetailResponseDto(
                paymentId,
                orderId,
                userId,
                10000,
                status,
                paymentKey,
                LocalDateTime.now()
        );
    }
}
