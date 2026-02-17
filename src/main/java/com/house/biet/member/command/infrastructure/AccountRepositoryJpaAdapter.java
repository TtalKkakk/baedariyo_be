package com.house.biet.member.command.infrastructure;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public Optional<Account> findById(Long accountId) {
        return accountRepositoryJpa.findById(accountId);
    }

    @Override
    public List<Account> findAll() {
        return accountRepositoryJpa.findAll();
    }

    @Override
    public boolean existsByEmailAndRole(Email email, UserRole role) {
        return accountRepositoryJpa.existsByEmailAndRole(email, role);
    }

    @Override
    public Optional<Account> findByEmailAndRole(Email email, UserRole role) {
        return accountRepositoryJpa.findByEmailAndRole(email, role);
    }
}
