package com.house.biet.payment.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PaymentKey {

    private String value;

    public PaymentKey(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_PAYMENT_KEY);
        }
        this.value = value;
    }
}
