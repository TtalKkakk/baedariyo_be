package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.payment.command.application.PaymentCreateService;
import com.house.biet.payment.command.application.PaymentService;
import com.house.biet.payment.command.application.dto.PaymentApproveRequestDto;
import com.house.biet.payment.command.application.dto.PaymentCreateRequestDto;
import com.house.biet.payment.query.application.PaymentQueryMapper;
import com.house.biet.payment.query.application.PaymentQueryService;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 결제 관련 REST API Controller.
 *
 * <p>
 * 결제 생성, 승인, 실패, 취소 등의 명령(Command) 요청을 처리하며,
 * 조회는 PaymentQueryService를 통해 제공된다.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentQueryService paymentQueryService;
    private final PaymentCreateService paymentCreateService;
    private final PaymentQueryMapper paymentQueryMapper;

    /**
     * 새로운 결제를 생성한다.
     *
     * @param principal 인증 사용자 정보
     * @param requestDto 주문 ID, 금액, paymentKey를 포함한 JSON 요청
     * @return 생성된 결제 ID
     */
    @PostMapping
    public ResponseEntity<CustomApiResponse<Long>> createPayment(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody PaymentCreateRequestDto requestDto
    ) {
        Long orderId = requestDto.orderId();
        Money money = new Money(requestDto.amount());
        String paymentKey = requestDto.paymentKey();

        Long paymentId = paymentCreateService.createPayment(orderId, money, paymentKey, principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.PAYMENT_CREATE_SUCCESS, paymentId)
        );
    }

    /**
     * 결제 승인 처리.
     *
     * @param paymentId 결제 ID
     * @param requestDto PG에서 발급한 transactionId 포함
     * @return 성공 여부
     */
    @PostMapping("/{paymentId}/approve")
    public ResponseEntity<CustomApiResponse<Void>> approvePayment(
            @PathVariable Long paymentId,
            @RequestBody PaymentApproveRequestDto requestDto
    ) {
        paymentService.approve(paymentId, requestDto.transactionId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.PAYMENT_APPROVE_SUCCESS, null)
        );
    }

    /**
     * 결제 승인 실패 처리.
     *
     * @param paymentId 결제 ID
     * @return 성공 여부
     */
    @PostMapping("/{paymentId}/fail")
    public ResponseEntity<CustomApiResponse<Void>> failPayment(
            @PathVariable Long paymentId
    ) {
        paymentService.fail(paymentId);
        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.PAYMENT_FAIL_SUCCESS, null)
        );
    }

    /**
     * 결제 취소 처리.
     *
     * @param paymentId 결제 ID
     * @return 성공 여부
     */
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<CustomApiResponse<Void>> cancelPayment(
            @PathVariable Long paymentId
    ) {
        paymentService.cancel(paymentId);
        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.PAYMENT_CANCEL_SUCCESS, null)
        );
    }

    // ================== Query ==================

    /**
     * 결제 단건 조회
     *
     * @param paymentId 결제 식별자
     * @return getPayment 결과
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<CustomApiResponse<PaymentDetailResponseDto>> getPayment(
            @PathVariable Long paymentId
    ) {
        PaymentDetailResponseDto responseDto = paymentQueryMapper.getPayment(paymentId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.PAYMENT_GET_SUCCESS, responseDto)
        );
    }

    /**
     * 내 결제 목록 조회 (상태 필터 가능)
     *
     * @param principal 인증 사용자 정보
     * @param status 상태
     * @return getMyPayments 결과
     */
    @GetMapping("/my")
    public ResponseEntity<CustomApiResponse<List<MyPaymentDetailResponseDto>>> getMyPayments(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestParam(required = false) PaymentStatus status
    ) {

        List<MyPaymentDetailResponseDto> responseDtos = paymentQueryMapper.getMyPaymentList(principal.accountId(), status);

        return ResponseEntity.ok(
                CustomApiResponse.success(
                        SuccessCode.PAYMENT_GET_LIST_SUCCESS,
                        responseDtos
                )
        );
    }
}