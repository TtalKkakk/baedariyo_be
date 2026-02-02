package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthUserService;
import com.house.biet.auth.command.domain.dto.UserSignupRequestDto;
import com.house.biet.user.command.application.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSignupService {

    private final AuthUserService authUserService;
    private final UserService userService;

    public void signup(UserSignupRequestDto dto) {
        authUserService.signup(dto.email(), dto.password());
        userService.save(dto.name(), dto.nickname(), dto.phoneNumber());
    }
}
