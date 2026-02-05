package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;

@Embeddable
public class Money {

    private final int amount;

    public Money(int amount) {
        if (amount < 0)
            throw new CustomException(ErrorCode.INVALID_MONEY_AMOUNT);

        this.amount = amount;
    }

    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    public int value() {
        return amount;
    }
}
