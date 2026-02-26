package com.house.biet.payment.query.application.dto;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;

public record MyPaymentSearchCondition(
        Long userId,
        PaymentStatus paymentStatus
) {
    public MyPaymentSearchCondition {
        if (userId == null)
            throw new CustomException(ErrorCode.INVALID_INPUT_USER_ID);
    }
}
