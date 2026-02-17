package com.house.biet.fixtures;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class AccountFixtures {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private AccountFixtures() {}

    public static Account account(UserRole userRole) {
        return Account.signup(
                new Email(userRole.toString() + UUID.randomUUID().toString().substring(1, 10) + "@test.com"),
                Password.encrypt(UUID.randomUUID().toString().substring(1, 30), ENCODER),
                userRole
        );
    }
}
