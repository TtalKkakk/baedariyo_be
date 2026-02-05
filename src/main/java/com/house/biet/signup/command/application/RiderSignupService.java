package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.RiderSignupRequestDto;
import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.rider.command.application.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderSignupService {

    private final AuthService authService;
    private final RiderService riderService;

    public void signup(RiderSignupRequestDto dto) {
        Account account = authService.signup(dto.email(), dto.password(), UserRole.RIDER);
        riderService.save(account, dto.name(), dto.nickname(), dto.phoneNumber(), dto.vehicleType());
    }
}
