package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.UserAccountRepository;
import com.house.biet.user.command.domain.entity.UserAccount;
import com.house.biet.member.domain.vo.Email;
import com.house.biet.member.domain.vo.Password;
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
public class AuthUserServiceIntegrationTest {

    @Autowired
    AuthUserService authUserService;

    @Autowired
    UserAccountRepository userAccountRepository;

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

        UserAccount userAccount = UserAccount.signUp(email, password);
        userAccountRepository.save(userAccount);
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
        LoginResultDto result = authUserService.login(emailValue, passwordValue);

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
        assertThatThrownBy(() -> authUserService.login(notExistEmailValue, randomPasswordValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
    }
}
