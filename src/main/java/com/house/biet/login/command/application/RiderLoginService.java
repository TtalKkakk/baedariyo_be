package com.house.biet.login.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.application.dto.AuthLoginResultDto;
import com.house.biet.auth.command.domain.dto.LoginResultDto;
import com.house.biet.auth.command.domain.dto.LoginRequestDto;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.rider.command.application.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderLoginService {

    private final AuthService authService;
    private final RiderService riderService;

    public LoginResultDto login(LoginRequestDto requestDto) {
        AuthLoginResultDto authLoginResultDto = authService.login(requestDto.email(), requestDto.password(), UserRole.RIDER);
        riderService.markOnlineIfOffline(authLoginResultDto.accountId());

        return new LoginResultDto(
                authLoginResultDto.accessToken(),
                authLoginResultDto.refreshToken()
        );
    }
}
