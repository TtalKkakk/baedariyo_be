package com.house.biet.member.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PhoneNumber {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^01[0-9]-\\d{3,4}-\\d{4}$");

    private String value;

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
        }

        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new CustomException(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
        }

        this.value = value;
    }
}
