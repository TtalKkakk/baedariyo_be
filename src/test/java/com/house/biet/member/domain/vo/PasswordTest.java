package com.house.biet.member.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Test
    @DisplayName("성공: 비밀번호 생성")
    void createPassword_Success() {
        // given
        String rawPassword1 = "iLO0E@JuX4GFs15";  // 15
        String rawPassword2 = "x8OGfZRRt1@osqnzUkWVV!0VTGwCkUHvYI04eaBC";  // 40

        // when
        Password madePassword1 = Password.encrypt(rawPassword1, ENCODER);
        Password madePassword2 = Password.encrypt(rawPassword2, ENCODER);

        // then
        assertThat(madePassword1.matches(rawPassword1, ENCODER)).isTrue();
        assertThat(madePassword2.matches(rawPassword2, ENCODER)).isTrue();
    }

    @Test
    @DisplayName("성공: 비밀번호 매칭 실패")
    void notMatchEncryptedPassword_Success() {
        // given
        String rawPassword = "x8OGfZRRt1@osqnvYI04eaBC";

        // when
        String wrongPassword = "sielkj@issjvkl9!xzkjs";
        Password madePassword = Password.encrypt(rawPassword, ENCODER);

        // then
        assertThat(madePassword.matches(wrongPassword, ENCODER)).isFalse();
    }

    @Test
    @DisplayName("에러: 비밀번호 생성 - 길이 에러")
    void createPassword_Error_InvalidPasswordLength() {
        // given
        String tooShortPassword = "iLO0E@JuX4GFs1";  // 14
        String tooLongPassword = "x8OGfZRRt1@osqnzUkWVV!0VTGwCkUHvYI04eaBCD";  // 41

        // when & then
        assertThatThrownBy(() -> Password.encrypt(tooShortPassword, ENCODER))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_FORMAT.getMessage());

        assertThatThrownBy(() -> Password.encrypt(tooLongPassword, ENCODER))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_FORMAT.getMessage());
    }
}