package com.house.biet.store.command.domain.vo;

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
public class StoreName {

    private String value;

    public StoreName(String value) {
        if (value == null || value.isBlank())
            throw new CustomException(ErrorCode.INVALID_STORE_NAME_FORMAT);
        if (value.length() > 50)
            throw new CustomException(ErrorCode.INVALID_STORE_NAME_FORMAT);

        this.value = value;
    }
}
