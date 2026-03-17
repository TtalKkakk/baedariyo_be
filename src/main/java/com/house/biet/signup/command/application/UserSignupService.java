package com.house.biet.signup.command.application;

import com.house.biet.auth.command.application.AuthService;
import com.house.biet.auth.command.domain.dto.UserSignupRequestDto;
import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
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

    public void signup(
            String email,
            String password,
            String name,
            String nickname,
            String phoneNumber,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String addressAlias
    ) {
        Account account = authService.signup(email, password, UserRole.USER);

        userService.save(
                account,
                name,
                nickname,
                phoneNumber,
                roadAddress,
                jibunAddress,
                detailAddress,
                addressAlias
        );
    }
}
