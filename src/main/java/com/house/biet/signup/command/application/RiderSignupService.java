package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.RiderSignupRequestDto;
import com.house.biet.global.vo.UserRole;
import com.house.biet.rider.command.application.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderSignupService {

    private final AuthService authService;
    private final RiderService riderService;

    public void signup(RiderSignupRequestDto dto) {
        authService.signup(dto.email(), dto.password(), UserRole.RIDER);
        riderService.save(dto.name(), dto.nickname(), dto.phoneNumber(), dto.vehicleType());
    }
}
