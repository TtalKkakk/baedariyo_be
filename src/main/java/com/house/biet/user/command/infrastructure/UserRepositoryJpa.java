package com.house.biet.user.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.user.command.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepositoryJpa
        extends JpaRepository<User, Long> {

    Optional<User> findByNickname(Nickname nickname);

    @Query("select u.id from User u where u.account.id = :accountId")
    Optional<Long> findUserIdByAccountId(@Param("accountId") Long accountId);
}
