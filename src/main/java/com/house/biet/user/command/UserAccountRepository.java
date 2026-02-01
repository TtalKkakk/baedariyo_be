package com.house.biet.user.command;

import com.house.biet.user.command.domain.entity.UserAccount;
import com.house.biet.user.command.domain.vo.Email;

import java.util.Optional;

public interface UserAccountRepository {

    UserAccount save(UserAccount userAccount);

    boolean existsByEmail(Email email);

    Optional<UserAccount> findByEmail(Email email);
}
