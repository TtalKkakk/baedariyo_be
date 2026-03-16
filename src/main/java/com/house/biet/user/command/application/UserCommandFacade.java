package com.house.biet.user.command.application;

import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandFacade {

    private final UserService userService;
    private final UserQueryService userQueryService;

    public void changeNickname(Long accountId, String newNicknameValue) {
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        userService.changeNicknameByUserId(userId, newNicknameValue);
    }

    public void changePhoneNumber(Long accountId, String newPhoneNumberValue) {
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        userService.changePhoneNumberByUserId(userId, newPhoneNumberValue);
    }
}
