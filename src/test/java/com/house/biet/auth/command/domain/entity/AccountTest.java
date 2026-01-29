package com.house.biet.auth.command.domain.entity;

import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
import com.house.biet.user.command.domain.vo.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

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
        Account account = Account.signUp(givenEmail, givenEncryptedPassword);

        // then
        assertThat(account.getEmail().getValue()).isEqualTo(givenEmailValue);
        assertThat(account.getPassword().matches(givenPasswordValue, ENCODER)).isTrue();
        assertThat(account.getRole()).isEqualTo(userRole);
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
        Account account = Account.signUp(givenEmail, givenEncryptedPassword);

        // when
        boolean matched = account.matchedPassword(givenPasswordValue, ENCODER);

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
        Account account = Account.signUp(givenEmail, givenEncryptedPassword);

        String anotherPasswordValue = "<RANDOM_PASSWORD>";

        // when
        boolean matched = account.matchedPassword(anotherPasswordValue, ENCODER);

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
        Account account = Account.signUp(givenEmail, givenEncryptedPassword);

        String newPasswordValue = "akl2da12keh@ahc!sdkjxx";
        Password newPassword = Password.encrypt(newPasswordValue, ENCODER);

        // when
        account.changePassword(newPassword);

        // then
        assertThat(account.getPassword().matches(newPasswordValue, ENCODER)).isTrue();
    }
}