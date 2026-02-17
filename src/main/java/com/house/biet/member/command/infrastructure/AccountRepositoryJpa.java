package com.house.biet.member.command.infrastructure;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepositoryJpa
        extends JpaRepository<Account, Long> {

    boolean existsByEmailAndRole(Email email, UserRole role);
    Optional<Account> findByEmailAndRole(Email email, UserRole role);
}
