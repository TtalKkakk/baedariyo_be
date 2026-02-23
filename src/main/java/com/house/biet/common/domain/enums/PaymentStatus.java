package com.house.biet.common.domain.enums;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;

public enum PaymentStatus {
    READY,        // 결제 생성
    REQUESTED,    // PG 요청 보냄
    APPROVED,     // 결제 승인
    FAILED,       // 실패
    CANCELED;     // 취소

    public void validateTransition(PaymentStatus target) {

        boolean valid = switch (this) {
            case READY -> target == REQUESTED;
            case REQUESTED -> target == APPROVED || target == FAILED;
            case APPROVED -> target == CANCELED;
            default -> false;
        };

        if (!valid) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION);
        }
    }
}