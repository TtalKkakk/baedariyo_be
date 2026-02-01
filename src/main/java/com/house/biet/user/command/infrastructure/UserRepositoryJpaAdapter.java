package com.house.biet.user.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserRepositoryJpa userRepositoryJpa;

    @Override
    public User save(User user) {
        return userRepositoryJpa.save(user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepositoryJpa.findById(userId);
    }

    @Override
    public Optional<User> findByNickname(Nickname nickname) {
        return userRepositoryJpa.findByNickname(nickname);
    }
}
