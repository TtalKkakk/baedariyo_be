package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {

    @Autowired
    AuthService authService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    String emailValue = "abc@xyz.com";
    String passwordValue = "qEDDfLnFNQ!GepkntaI@uO3XaYT5";

    Email email;
    Password password;

    @BeforeEach
    void setUp() {
        email = new Email(emailValue);
        password = Password.encrypt(passwordValue, passwordEncoder);

        Account account = Account.signup(email, password, UserRole.USER );
        accountRepository.save(account);
    }

    @Test
    @DisplayName("성공 - 회원가입 성공")
    void signup_Success() {
        // when

    }

    @Test
    @DisplayName("성공 - 로그인 성공")
    void login_Success() {
        // when
        LoginResultDto result = authService.login(emailValue, passwordValue, UserRole.USER);

        // then
        assertThat(result.accessToken()).isNotNull();
        assertThat(result.refreshToken()).isNotNull();
    }

    @Test
    @DisplayName("에러 - 존재하지 않은 이메일로 로그인")
    void login_Error_NotFoundEmail() {
        // given
        String notExistEmailValue = "notExist@xyz.com";
        String randomPasswordValue = "jIVVjDgldziYWgVMJPkaC@zVx50";

        // when & then
        assertThatThrownBy(() -> authService.login(notExistEmailValue, randomPasswordValue, UserRole.USER))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
    }
}
