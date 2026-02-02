package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.UserSignupRequestDto;
import com.house.biet.global.vo.UserRole;
import com.house.biet.user.command.application.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSignupService {

    private final AuthService authService;
    private final UserService userService;

    public void signup(UserSignupRequestDto dto) {
        authService.signup(dto.email(), dto.password(), UserRole.USER);
        userService.save(dto.name(), dto.nickname(), dto.phoneNumber());
    }
}
