package com.house.biet.user.command;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.user.command.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByNickname(Nickname nickname);
}
