package com.house.biet.member.command.domain.vo;

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
public class Email {

    private String value;

    public Email(String value) {
        if (!value.contains("@") || !value.contains("."))
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);

        this.value = value;
    }
}
