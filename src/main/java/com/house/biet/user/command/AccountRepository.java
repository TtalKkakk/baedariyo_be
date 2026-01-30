package com.house.biet.user.command;

import com.house.biet.user.command.domain.entity.Account;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findByEmail(String value);
}
