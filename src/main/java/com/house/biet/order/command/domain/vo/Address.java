package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String fullAddressValue;

    public Address(String fullAddressValue) {
        if (fullAddressValue == null || fullAddressValue.isBlank())
            throw new CustomException(ErrorCode.INVALID_ADDRESS_FORMAT);

        this.fullAddressValue = fullAddressValue;
    }
}
