package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.domain.entity.UserAccount;
import com.house.biet.user.command.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepositoryJpa
        extends JpaRepository<UserAccount, Long> {

    boolean existsByEmail(Email email);
    Optional<UserAccount> findByEmail(Email email);
}
