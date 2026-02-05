package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.RiderSignupRequestDto;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.rider.command.application.RiderService;
import com.house.biet.rider.command.domain.vo.VehicleType;
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
class RiderSignupServiceTest {

    @InjectMocks
    private RiderSignupService riderSignupService;

    @Mock
    private AuthService authService;

    @Mock
    private RiderService riderService;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    RiderSignupRequestDto dto;

    Account account;

    @BeforeEach
    void setup() {
        dto = new RiderSignupRequestDto(
                "rider@biet.com",
                UUID.randomUUID().toString().substring(1, 30),
                "<REAL-NAME>",
                "<NICKNAME>",
                "010-1111-1111",
                VehicleType.MOTORCYCLE
        );


        account = Account.signup(
                new Email(dto.email()),
                Password.encrypt(dto.password(), ENCODER),
                UserRole.RIDER
        );
    }

    @Test
    @DisplayName("성공 - 라이더 회원가입 시 Account 와 Rider가 생성")
    void signup_success() {
        // given
        given(authService.signup(dto.email(), dto.password(), UserRole.RIDER))
                .willReturn(account);

        // when
        riderSignupService.signup(dto);

        // then
        then(authService).should()
                .signup(dto.email(), dto.password(), UserRole.RIDER);

        then(riderService).should()
                .save(
                        account,
                        dto.name(),
                        dto.nickname(),
                        dto.phoneNumber(),
                        dto.vehicleType()
                );
    }

    @Test
    @DisplayName("에러 - Account 생성 실패 시 Rider 생성은 호출되지 않는다")
    void signup_error_whenAuthFails() {
        // given
        willThrow(new RuntimeException("auth fail"))
                .given(authService)
                .signup(anyString(), anyString(), eq(UserRole.RIDER));

        // when & then
        assertThatThrownBy(() -> riderSignupService.signup(dto))
                .isInstanceOf(RuntimeException.class);

        then(riderService).shouldHaveNoInteractions();
    }
}
