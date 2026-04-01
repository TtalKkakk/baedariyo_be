package com.house.biet.user.command;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.user.command.domain.aggregate.User;

import java.util.Optional;

public interface UserRepository {

    /**
     * 대상을 저장한다
     *
     * @param user user 값
     * @return save 결과
     */
    User save(User user);

    /**
     * 식별자을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과
     */
    Optional<User> findById(Long userId);

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
    Optional<Long> findUserIdByAccountId(Long accountId);
}
