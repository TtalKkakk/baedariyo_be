package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.AccountRepository;
import com.house.biet.user.command.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryJpaAdapter implements AccountRepository {

    private final AccountRepositoryJpa accountRepositoryJpa;

    @Override
    public Account save(Account account) {
        return accountRepositoryJpa.save(account);
    }

    @Override
    public Optional<Account> findByEmail(String value) {
        return accountRepositoryJpa.findByEmail_value(value);
    }
}
