package com.house.biet.user.command;

import com.house.biet.user.command.domain.entity.Account;
import com.house.biet.user.command.domain.vo.Email;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    boolean existsByEmail(Email email);

    Optional<Account> findByEmail(Email email);
}
