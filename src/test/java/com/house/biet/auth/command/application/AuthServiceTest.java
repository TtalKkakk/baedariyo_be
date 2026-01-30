package com.house.biet.auth.command.application;

import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.AccountRepository;
import com.house.biet.user.command.domain.entity.Account;
import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
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

    @Test
    @DisplayName("성공 - 로그인 성공")
    void login_success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        Email givenEmail = new Email(givenEmailValue);

        String givenPasswordValue = "gsPqZwBlx@Wko2hihjaH!gB@peCJohn4ycIw8o";
        Password givenPassword = Password.encrypt(givenPasswordValue, ENCODER);

        Account account = Account.signUp(givenEmail, givenPassword);

        given(accountRepository.findByEmail(anyString()))
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
        assertThat(result.getAccessToken()).isEqualTo("access-token");
        assertThat(result.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("에러 - 존재하지 않은 이메일로 로그인")
    void login_Error_AccountNotFound() {
        // given
        given(accountRepository.findByEmail(anyString()))
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
        String existEmailValue = "abc@xyz.com";
        String existPasswordValue = "6BHUs9CsALGe5#LdH2kTy5piBJo@qJWIWU7bF";

        String notCorrectPasswordValue = "Kwo87x37T3vjSD!20HY@VeAC4#5DNbT6wuY";

        Account account = Account.signUp(new Email(existEmailValue), Password.encrypt(existPasswordValue, ENCODER));

        given(accountRepository.findByEmail(existEmailValue))
                .willReturn(Optional.of(account));

        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(existEmailValue, notCorrectPasswordValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_CORRECT_PASSWORD.getMessage());
    }
}
