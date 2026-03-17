package com.house.biet.user.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressAlias {

    private String value;

    public AddressAlias(String value) {
        if (value == null || value.isBlank() || value.length() > 10) {
            throw new CustomException(ErrorCode.INVALID_ADDRESS_ALIAS_FORMAT);
        }

        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
