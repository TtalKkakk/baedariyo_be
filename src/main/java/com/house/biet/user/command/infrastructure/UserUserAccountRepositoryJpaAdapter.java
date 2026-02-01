package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.UserAccountRepository;
import com.house.biet.user.command.domain.entity.UserAccount;
import com.house.biet.user.command.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserUserAccountRepositoryJpaAdapter implements UserAccountRepository {

    private final UserAccountRepositoryJpa userAccountRepositoryJpa;

    @Override
    public UserAccount save(UserAccount userAccount) {
        return userAccountRepositoryJpa.save(userAccount);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userAccountRepositoryJpa.existsByEmail(email);
    }

    @Override
    public Optional<UserAccount> findByEmail(Email email) {
        return userAccountRepositoryJpa.findByEmail(email);
    }
}
