package com.house.biet.user.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.user.command.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryJpa
        extends JpaRepository<User, Long> {

    Optional<User> findByNickname(Nickname nickname);
}
