package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.UserSignupRequestDto;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.user.command.application.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserSignupServiceTest {

    @InjectMocks
    private UserSignupService userSignupService;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    UserSignupRequestDto dto;

    Account account;

    @BeforeEach
    void setup() {
        dto = new UserSignupRequestDto(
                "abc@xyz.com",
                UUID.randomUUID().toString().substring(1, 30),
                "<REAL-NAME>",
                "<NICKNAME>",
                "010-1111-1111"
        );


        account = Account.signup(
                new Email(dto.email()),
                Password.encrypt(dto.password(), ENCODER),
                UserRole.USER
        );
    }

    @Test
    @DisplayName("성공 - 회원가입 시 Account 와 User가 생성")
    void signup_Success() {
        // given
        given(authService.signup(dto.email(), dto.password(), UserRole.USER))
                .willReturn(account);

        // when
        userSignupService.signup(dto);

        // then
        then(authService).should()
                .signup(dto.email(), dto.password(), UserRole.USER);

        then(userService).should()
                .save(account, dto.name(), dto.nickname(), dto.phoneNumber());
    }

    @Test
    @DisplayName("에러 - Account 생성 실패 시 User 생성은 호출되지 않는다")
    void signup_Error_WhenAuthFails() {
        // given
        willThrow(new RuntimeException("auth fail"))
                .given(authService)
                .signup(anyString(), anyString(), eq(UserRole.USER));

        // when & then
        assertThatThrownBy(() -> userSignupService.signup(dto))
                .isInstanceOf(RuntimeException.class);

        then(userService).shouldHaveNoInteractions();
    }

}