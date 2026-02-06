package com.house.biet.fixtures;

import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.user.command.domain.entity.User;

public class UserFixtures {

    private UserFixtures() {}

    public static User user(Account userAccount) {
        return User.create(
                userAccount,
                "테스트유저",
                "testUser",
                "010-0000-0000"
        );
    }
}
