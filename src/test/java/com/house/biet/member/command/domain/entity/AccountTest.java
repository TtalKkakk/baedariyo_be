package com.house.biet.member.command.domain.entity;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.common.domain.enums.AccountStatus;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.common.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmailValue = "abc@xyz.com";
    String givenPasswordValue = UUID.randomUUID().toString().substring(1, 30);
    UserRole userRole = UserRole.USER;

    Email givenEmail;
    Password givenEncryptedPassword;

    @BeforeEach
    void setup() {
        givenEmail = new Email(givenEmailValue);
        givenEncryptedPassword = Password.encrypt(givenPasswordValue, ENCODER);
    }


    @Test
    @DisplayName("성공 - 유저 계좌 생성 성공")
    void createAccess_Success() {
        // when
        Account account = Account.signup(givenEmail, givenEncryptedPassword, userRole);

        // then
        assertThat(account.getEmail().getValue()).isEqualTo(givenEmailValue);
        assertThat(account.getPassword().matches(givenPasswordValue, ENCODER)).isTrue();
        assertThat(account.getRole()).isEqualTo(userRole);
        assertThat(account.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
    }


    @Test
    @DisplayName("성공 - 계좌 삭제 시 정지 상태로 변경")
    void withdrawAccount_Success() {
        // given
        Account account = Account.signup(givenEmail, givenEncryptedPassword, userRole);

        // when
        account.withdraw();

        // then
        assertThat(account).isNotNull();
        assertThat(account.getAccountStatus()).isEqualTo(AccountStatus.WITHDRAWN);
    }

    @Test
    @DisplayName("에러 - 이미 삭제된 계좌를 삭제")
    void withDrawAccount_Error_AlreadyWithdrawn() {
        // given
        Account account = Account.signup(givenEmail, givenEncryptedPassword, userRole);

        ReflectionTestUtils.setField(account, "accountStatus", AccountStatus.WITHDRAWN);

        // when & then
        assertThatThrownBy(() -> account.withdraw())
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ALREADY_WITHDRAWN_ACCOUNT.getMessage());
    }

    @Test
    @DisplayName("성공 - 유저 비밀번호 매치 성공")
    void matchedPassword_Success() {
        // given
        Account account = Account.signup(givenEmail, givenEncryptedPassword, userRole);

        // when
        boolean matched = account.matchedPassword(givenPasswordValue, ENCODER);

        // then
        assertThat(matched).isTrue();
    }

    @Test
    @DisplayName("성공 - 비밀번호 매치 실패")
    void notMatchedPassword_Success() {
        // given
        Account account = Account.signup(givenEmail, givenEncryptedPassword, userRole);

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
        Account account = Account.signup(givenEmail, givenEncryptedPassword, userRole);

        String newPasswordValue = "akl2da12keh@ahc!sdkjxx";
        Password newPassword = Password.encrypt(newPasswordValue, ENCODER);

        // when
        account.changePassword(newPassword);

        // then
        assertThat(account.getPassword().matches(newPasswordValue, ENCODER)).isTrue();
    }
}