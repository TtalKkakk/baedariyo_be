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
    @DisplayName("성공 - 상태로 결제 목록을 조회한다")
    void findByStatus_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(1L, 1L, 2L, PaymentStatus.READY, "pk_status");
        given(paymentQueryRepository.findAllByStatus(PaymentStatus.READY)).willReturn(List.of(dto));

        // when
        List<PaymentDetailResponseDto> result = paymentQueryService.findByStatus(PaymentStatus.READY);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).orderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("성공 - 결제 ID로 결제 상세를 조회한다")
    void findById_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(2L, 2L, 3L, PaymentStatus.READY, "pk_o1");
        given(paymentQueryRepository.findById(2L)).willReturn(Optional.of(dto));

        // when
        Optional<PaymentDetailResponseDto> result = paymentQueryService.findById(2L);

        // then
        assertThat(result).contains(dto);
    }

    @Test
    @DisplayName("성공 - 주문 ID로 결제 목록을 조회한다")
    void findByOrderId_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(3L, 2L, 3L, PaymentStatus.READY, "pk_o1");
        given(paymentQueryRepository.findAllByOrderId(2L)).willReturn(List.of(dto));

        // when
        List<PaymentDetailResponseDto> result = paymentQueryService.findByOrderId(2L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).orderId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("성공 - 사용자 ID로 결제 목록을 조회한다")
    void findByUserId_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(4L, 3L, 10L, PaymentStatus.READY, "pk_user");
        given(paymentQueryRepository.findAllByUserId(10L)).willReturn(List.of(dto));

        // when
        List<PaymentDetailResponseDto> result = paymentQueryService.findByUserId(10L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).userId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("성공 - 사용자 ID와 상태 목록으로 결제 목록을 조회한다")
    void findByUserIdAndStatuses_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(5L, 4L, 20L, PaymentStatus.READY, "pk_multi");
        given(paymentQueryRepository.findAllByUserIdAndStatusIn(20L, List.of(PaymentStatus.READY))).willReturn(List.of(dto));

        // when
        List<PaymentDetailResponseDto> result = paymentQueryService.findByUserIdAndStatuses(20L, List.of(PaymentStatus.READY));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).userId()).isEqualTo(20L);
    }

    @Test
    @DisplayName("성공 - 주문 ID로 승인된 결제를 조회한다")
    void findApprovedByOrderId_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(6L, 5L, 30L, PaymentStatus.APPROVED, "pk_approved");
        given(paymentQueryRepository.findApprovedByOrderId(5L)).willReturn(Optional.of(dto));

        // when
        Optional<PaymentDetailResponseDto> result = paymentQueryService.findApprovedByOrderId(5L);

        // then
        assertThat(result).contains(dto);
        assertThat(result.get().status()).isEqualTo(PaymentStatus.APPROVED);
    }

    @Test
    @DisplayName("성공 - 승인된 결제가 없으면 빈 결과를 반환한다")
    void findApprovedByOrderId_Success_WhenApprovedPaymentDoesNotExist() {
        // given
        given(paymentQueryRepository.findApprovedByOrderId(99L)).willReturn(Optional.empty());

        // when
        Optional<PaymentDetailResponseDto> result = paymentQueryService.findApprovedByOrderId(99L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("성공 - paymentKey로 결제를 조회한다")
    void findByPaymentKey_Success() {
        // given
        PaymentDetailResponseDto dto = paymentDetail(7L, 6L, 40L, PaymentStatus.READY, "pk_find");
        given(paymentQueryRepository.findByPaymentKey("pk_find")).willReturn(Optional.of(dto));

        // when
        Optional<PaymentDetailResponseDto> result = paymentQueryService.findByPaymentKey("pk_find");

        // then
        assertThat(result).contains(dto);
        assertThat(result.get().paymentKey()).isEqualTo("pk_find");
    }

    @Test
    @DisplayName("성공 - paymentKey에 해당하는 결제가 없으면 빈 결과를 반환한다")
    void findByPaymentKey_Success_WhenPaymentDoesNotExist() {
        // given
        given(paymentQueryRepository.findByPaymentKey("not_exist")).willReturn(Optional.empty());

        // when
        Optional<PaymentDetailResponseDto> result = paymentQueryService.findByPaymentKey("not_exist");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("성공 - 사용자 결제가 없으면 빈 목록을 반환한다")
    void findByUserId_Success_WhenPaymentsDoNotExist() {
        // given
        given(paymentQueryRepository.findAllByUserId(123L)).willReturn(List.of());

        // when
        List<PaymentDetailResponseDto> result = paymentQueryService.findByUserId(123L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("성공 - 상태 조건 없이 내 결제 상세 목록을 조회한다")
    void findMyPaymentDetailList_Success_WhenStatusIsNull() {
        // given
        Long userId = 1L;
        MyPaymentSearchCondition condition = new MyPaymentSearchCondition(userId, null);

        MyPaymentDetailResponseDto dto =
                new MyPaymentDetailResponseDto(
                        1L,
                        "store-1",
                        100L,
                        "store1",
                        PaymentStatus.READY,
                        null,
                        List.of(),
                        null,
                        List.of(),
                        10000,
                        null
                );

        given(paymentQueryRepository.findMyPaymentDetailList(condition))
                .willReturn(List.of(dto));

        // when
        List<MyPaymentDetailResponseDto> result =
                paymentQueryService.findMyPaymentDetailList(condition);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).storeName()).isEqualTo("store1");
    }

    @Test
    @DisplayName("성공 - 상태 조건으로 내 결제 상세 목록을 조회한다")
    void findMyPaymentDetailList_Success_WhenStatusExists() {
        // given
        Long userId = 2L;
        MyPaymentSearchCondition condition =
                new MyPaymentSearchCondition(userId, PaymentStatus.APPROVED);

        MyPaymentDetailResponseDto dto =
                new MyPaymentDetailResponseDto(
                        2L,
                        "store-2",
                        200L,
                        "store2",
                        PaymentStatus.APPROVED,
                        5,
                        List.of(),
                        "good",
                        List.of("img1"),
                        20000,
                        null
                );

        given(paymentQueryRepository.findMyPaymentDetailList(condition))
                .willReturn(List.of(dto));

        // when
        List<MyPaymentDetailResponseDto> result =
                paymentQueryService.findMyPaymentDetailList(condition);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).paymentStatus()).isEqualTo(PaymentStatus.APPROVED);
    }

    private PaymentDetailResponseDto paymentDetail(Long paymentId, Long orderId, Long userId, PaymentStatus status, String paymentKey) {
        return new PaymentDetailResponseDto(paymentId, orderId, userId, 10000, status, paymentKey, LocalDateTime.now());
    }
}