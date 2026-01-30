package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.domain.entity.Account;
import com.house.biet.user.command.AccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepositoryJpa
        extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail_value(String value);
}
