package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmailValue = "abc@xyz.com";
    String givenPasswordValue = "gsPqZwBlx@Wko2hihjaH!gB@peCJohn4ycIw8o";

    Email givenEmail;
    Password givenPassword;
    Account account;

    @BeforeEach
    void setup() {
        givenEmail = new Email(givenEmailValue);
        givenPassword = Password.encrypt(givenPasswordValue, ENCODER);
        account = Account.signUp(givenEmail, givenPassword, UserRole.USER);
    }

    @Test
    @DisplayName("성공 - 회원가입 성공")
    void signup_Success() {
        // when
        given(accountRepository.existsByEmailAndRole(any(Email.class), eq(UserRole.USER)))
                .willReturn(false);

        // when
        authService.signup(givenEmailValue, givenPasswordValue);

        // then
        verify(accountRepository, times(1))
                .save(any(Account.class));
    }

    @Test
    @DisplayName("에러 - 이미 존재하는 이메일입니다")
    void signup_Error_ExistEmailValue() {
        // when
        given(accountRepository.existsByEmailAndRole(any(Email.class), eq(UserRole.USER)))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.signup(givenEmailValue, givenPasswordValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ALREADY_EXIST_EMAIL_AND_ROLE.getMessage());
    }

    @Test
    @DisplayName("성공 - 로그인 성공")
    void login_success() {
        // given
        given(accountRepository.findByEmailAndRole(any(Email.class), eq(UserRole.USER)))
                .willReturn(Optional.of(account));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);

        given(jwtProvider.createAccessToken(any(), anyString()))
                .willReturn("access-token");

        given(jwtProvider.createRefreshToken(any(), anyString()))
                .willReturn("refresh-token");

        // when
        LoginResultDto result = authService.login(givenEmailValue, givenPasswordValue);

        // then
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("에러 - 존재하지 않은 이메일로 로그인")
    void login_Error_AccountNotFound() {
        // given
        given(accountRepository.findByEmailAndRole(any(Email.class), eq(UserRole.USER)))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login("notInvalidId@xyz.com", "<RANDOM_NOT_SHORT_PASSWORD>"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ACCOUNT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("에러 - 로그인 실패")
    void login_InvalidUserInformation() {
        // given
        String notCorrectPasswordValue = "Kwo87x37T3vjSD!20HY@VeAC4#5DNbT6wuY";

        given(accountRepository.findByEmailAndRole(givenEmail, UserRole.USER))
                .willReturn(Optional.of(account));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(givenEmailValue, notCorrectPasswordValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_CORRECT_PASSWORD.getMessage());
    }
}
