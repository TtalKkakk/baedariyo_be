package com.house.biet.member.command;

import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    List<Account> findAll();

    boolean existsByEmailAndRole(Email email, UserRole role);

    Optional<Account> findByEmailAndRole(Email email, UserRole role);
}
