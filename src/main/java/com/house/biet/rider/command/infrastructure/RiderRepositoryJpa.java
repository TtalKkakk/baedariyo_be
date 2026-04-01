package com.house.biet.rider.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.rider.command.domain.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RiderRepositoryJpa
        extends JpaRepository<Rider, Long> {

    /**
     * 식별자 닉네임을 조회한다
     *
     * @param nickname nickname 값
     * @return 조회 결과
     */
    @Query("select r.id from Rider r where r.nickname.value = :nickname")
    Optional<Long> findIdByNickname(@Param("nickname") String nickname);

    /**
     * 라이더 식별자 계정 식별자을 조회한다
     *
     * @param accountId 계정 식별자
     * @return 조회 결과
     */
    @Query("select r.id from Rider r where r.account.id = :accountId")
    Optional<Long> findRiderIdByAccountId(@Param("accountId") Long accountId);
}
