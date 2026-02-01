package com.house.biet.member.domain.vo;

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
public class RealName {

    private String value;

    public RealName(String value) {
        if (value == null || value.isBlank())
            throw new CustomException(ErrorCode.INVALID_REAL_NAME_FORMAT);
        
        // TODO: 실제 사용자 인증 가능할 시 추가할 것
        
        this.value = value;
    }
}
