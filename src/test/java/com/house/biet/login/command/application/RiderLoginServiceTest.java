package com.house.biet.login.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.application.dto.AuthLoginResultDto;
import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.command.domain.dto.LoginRequestDto;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.rider.command.application.RiderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RiderLoginServiceTest {

    @InjectMocks
    private RiderLoginService riderLoginService;

    @Mock
    private AuthService authService;

    @Mock
    private RiderService riderService;

    LoginRequestDto requestDto;

    @BeforeEach
    void setup() {
        requestDto = new LoginRequestDto(
                "rider@biet.com",
                "password123"
        );
    }

    @Test
    @DisplayName("성공 - 라이더 로그인 시 토큰 발급 및 라이더 온라인 상태 전환")
    void login_success() {
        // given
        AuthLoginResultDto authResult = new AuthLoginResultDto(
                1L,
                "access-token",
                "refresh-token"
        );

        given(authService.login(
                requestDto.email(),
                requestDto.password(),
                UserRole.RIDER
        )).willReturn(authResult);

        // when
        LoginResultDto result = riderLoginService.login(requestDto);

        // then
        then(authService).should()
                .login(requestDto.email(), requestDto.password(), UserRole.RIDER);

        then(riderService).should()
                .markOnlineIfOffline(1L);

        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");
    }
}
