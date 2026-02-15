package com.house.biet.common.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 금액을 표현하는 값 객체
 *
 * <p>
 * - 음수 금액을 허용하지 않음
 * - 모든 금액 연산은 새로운 Money 객체를 반환
 * - 불변성을 유지하는 Value Object
 * </p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Money {

    /**
     * 금액 값 (단위: 원)
     */
    private int amount;


    public Money(int amount) {
        if (amount < 0)
            throw new CustomException(ErrorCode.INVALID_MONEY_AMOUNT);

        this.amount = amount;
    }

    /**
     * 금액 덧셈
     *
     * @param other 더할 금액
     * @return 더해진 새로운 Money 객체
     */
    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    /**
     * 금액 곱셈
     *
     * @param quantity 수량
     * @return 곱해진 새로운 Money 객체
     */
    public Money multiply(int quantity) {
        return new Money(this.amount * quantity);
    }

    /**
     * 금액 값 반환
     *
     * @return 금액
     */
    public int value() {
        return amount;
    }
}
