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

    /**
     * 대상을 처리한다
     *
     * @param email email 값
     * @param password password 값
     * @param name name 값
     * @param nickname nickname 값
     * @param phoneNumber phoneNumber 값
     * @param roadAddress 도로명 주소
     * @param jibunAddress 지번 주소
     * @param detailAddress 상세 주소
     * @param addressAlias 주소 별칭
     */
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
