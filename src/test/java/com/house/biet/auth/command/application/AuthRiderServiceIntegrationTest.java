package com.house.biet.auth.command.application;

import com.house.biet.auth.command.application.dto.AuthLoginResultDto;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.support.config.ServiceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthRiderServiceIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    AuthService authService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String emailValue = "rider@xyz.com";
    String passwordValue = "qEDDfLnFNQ!GepkntaI@uO3XaYT5";

    Email email;
    Password password;

    @BeforeEach
    void setUp() {
        email = new Email(emailValue);
        password = Password.encrypt(passwordValue, passwordEncoder);

        Account account = Account.signup(email, password, UserRole.RIDER);
        accountRepository.save(account);
    }

    @Test
    @DisplayName("성공 - 라이더 회원가입 성공")
    void signup_Success() {
        // given
        String newEmailValue = "new-rider@xyz.com";
        String newPasswordValue = "QwErTy!123456789";

        // when
        authService.signup(newEmailValue, newPasswordValue, UserRole.RIDER);

        // then
        Optional<Account> savedAccount = accountRepository.findByEmailAndRole(new Email(newEmailValue), UserRole.RIDER);

        assertThat(savedAccount).isPresent();

        Account account = savedAccount.orElseThrow();
        assertThat(account.getEmail().getValue()).isEqualTo(newEmailValue);
        assertThat(passwordEncoder.matches(newPasswordValue, account.getPassword().getValue())).isTrue();
        assertThat(account.getRole()).isEqualTo(UserRole.RIDER);
    }

    @Test
    @DisplayName("성공 - 라이더 로그인 성공")
    void login_Success() {
        // when
        AuthLoginResultDto result =
                authService.login(emailValue, passwordValue, UserRole.RIDER);

        // then
        assertThat(result.accountId()).isNotNull();
        assertThat(result.accessToken()).isNotNull();
        assertThat(result.refreshToken()).isNotNull();
    }

    @Test
    @DisplayName("에러 - 존재하지 않는 이메일로 라이더 로그인")
    void login_Error_NotFoundEmail() {
        // given
        String notExistEmailValue = "notExist@xyz.com";
        String randomPasswordValue = "jIVVjDgldziYWgVMJPkaC@zVx50";

        // when & then
        assertThatThrownBy(() -> authService.login(notExistEmailValue, randomPasswordValue, UserRole.RIDER))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
    }
}
