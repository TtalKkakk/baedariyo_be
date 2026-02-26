package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.query.application.dto.MyPaymentDetailResponseDto;
import com.house.biet.payment.query.application.dto.MyPaymentSearchCondition;
import com.house.biet.payment.query.application.dto.PaymentDetailResponseDto;
import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentQueryMapper {

    private final PaymentQueryService paymentQueryService;
    private final UserQueryService userQueryService;

    public PaymentDetailResponseDto getPayment(Long paymentId) {
        Payment payment = paymentQueryService.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        return PaymentDetailResponseDto.from(payment);
    }

    public List<MyPaymentDetailResponseDto> getMyPaymentList(Long accountId, PaymentStatus status) {
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        return paymentQueryService.findMyPaymentDetailList(
                new MyPaymentSearchCondition(userId, status)
        );
    }
}
