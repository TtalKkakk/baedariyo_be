package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.domain.entity.Account;
import com.house.biet.user.command.AccountRepository;
import com.house.biet.user.command.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepositoryJpa
        extends JpaRepository<Account, Long> {

    boolean existsByEmail(Email email);
    Optional<Account> findByEmail(Email email);
}
