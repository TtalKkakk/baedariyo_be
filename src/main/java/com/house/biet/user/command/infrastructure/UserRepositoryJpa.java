package com.house.biet.user.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.user.command.domain.aggregate.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepositoryJpa
        extends JpaRepository<User, Long> {

    /**
     * 닉네임을 조회한다
     *
     * @param nickname nickname 값
     * @return 조회 결과
     */
    Optional<User> findByNickname(Nickname nickname);

    /**
     * 사용자 식별자 계정 식별자을 조회한다
     *
     * @param accountId 계정 식별자
     * @return 조회 결과
     */
    @Query("select u.id from User u where u.account.id = :accountId")
    Optional<Long> findUserIdByAccountId(@Param("accountId") Long accountId);
}
