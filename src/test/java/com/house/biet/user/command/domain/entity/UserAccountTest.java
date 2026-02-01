package com.house.biet.user.command.domain.entity;

import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
import com.house.biet.user.command.domain.vo.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserAccountTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Test
    @DisplayName("성공 - 계좌 생성 성공")
    void createAccess_Success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        Email givenEmail = new Email(givenEmailValue);

        String givenPasswordValue = "a@lkdslkj!slkjxd";
        Password givenEncryptedPassword = Password.encrypt(givenPasswordValue, ENCODER);

        UserRole userRole = UserRole.USER;

        // when
        UserAccount userAccount = UserAccount.signUp(givenEmail, givenEncryptedPassword);

        // then
        assertThat(userAccount.getEmail().getValue()).isEqualTo(givenEmailValue);
        assertThat(userAccount.getPassword().matches(givenPasswordValue, ENCODER)).isTrue();
        assertThat(userAccount.getRole()).isEqualTo(userRole);
    }

    @Test
    @DisplayName("성공 - 비밀번호 매치 성공")
    void matchedPassword_Success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        Email givenEmail = new Email(givenEmailValue);

        String givenPasswordValue = "a@lkdslkj!slkjxd";
        Password givenEncryptedPassword = Password.encrypt(givenPasswordValue, ENCODER);

        UserRole userRole = UserRole.USER;
        UserAccount userAccount = UserAccount.signUp(givenEmail, givenEncryptedPassword);

        // when
        boolean matched = userAccount.matchedPassword(givenPasswordValue, ENCODER);

        // then
        assertThat(matched).isTrue();
    }
    
    @Test
    @DisplayName("성공 - 비밀번호 매치 실패")
    void notMatchedPassword_Success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        Email givenEmail = new Email(givenEmailValue);

        String givenPasswordValue = "a@lkdslkj!slkjxd";
        Password givenEncryptedPassword = Password.encrypt(givenPasswordValue, ENCODER);

        UserRole userRole = UserRole.USER;
        UserAccount userAccount = UserAccount.signUp(givenEmail, givenEncryptedPassword);

        String anotherPasswordValue = "<RANDOM_PASSWORD>";

        // when
        boolean matched = userAccount.matchedPassword(anotherPasswordValue, ENCODER);

        // then
        assertThat(matched).isFalse();
    }

    @Test
    @DisplayName("성공 - 비밀번호 변경 성공")
    void changePassword_Success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        Email givenEmail = new Email(givenEmailValue);

        String givenPasswordValue = "a@lkdslkj!slkjxd";
        Password givenEncryptedPassword = Password.encrypt(givenPasswordValue, ENCODER);

        UserRole userRole = UserRole.USER;
        UserAccount userAccount = UserAccount.signUp(givenEmail, givenEncryptedPassword);

        String newPasswordValue = "akl2da12keh@ahc!sdkjxx";
        Password newPassword = Password.encrypt(newPasswordValue, ENCODER);

        // when
        userAccount.changePassword(newPassword);

        // then
        assertThat(userAccount.getPassword().matches(newPasswordValue, ENCODER)).isTrue();
    }
}