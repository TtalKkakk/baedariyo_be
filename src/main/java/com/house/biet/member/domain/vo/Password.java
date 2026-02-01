package com.house.biet.member.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Password {

    private String value;

    private Password(String value) {
        this.value = value;
    }

    private static void validateRaw(String value) {
        // 길이 제한 (15 ~ 40)
        if (value.length() < 15 || value.length() > 40)
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);
        
        // TODO: 추가 조건 처리 할것
    }

    public static Password encrypt(String rawPassword, PasswordEncoder encoder) {
        validateRaw(rawPassword);
        return new Password(encoder.encode(rawPassword));
    }

    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, value);
    }
}
