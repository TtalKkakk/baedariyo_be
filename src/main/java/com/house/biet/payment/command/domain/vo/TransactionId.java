package com.house.biet.payment.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PG(결제 대행사)에서 발급하는 외부 승인 식별자.
 *
 * <p>
 * Payment가 APPROVED 상태로 전이될 때 반드시 존재해야 한다.
 * 도메인 내부에서 생성하지 않으며,
 * 외부 시스템(PG)의 응답을 통해 주입된다.
 * </p>
 *
 * <p>
 * 불변 객체이며, null 또는 blank 값을 허용하지 않는다.
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class TransactionId {

    private String value;

    public TransactionId(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_TRANSACTION_ID);
        }
        this.value = value;
    }
}