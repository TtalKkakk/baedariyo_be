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
public class Nickname {

    private String value;

    /**
     * 닉네임은 5~15 자 사이이며 중복되지 않아야 함
     *
     * @param value: nickname
     */
    public Nickname(String value) {
        if (value == null || value.isBlank())
            throw new CustomException(ErrorCode.INVALID_NICK_NAME_FORMAT);
        if (value.length() < 5 || value.length() > 15)
            throw new CustomException(ErrorCode.INVALID_NICK_NAME_FORMAT);
    }
}
