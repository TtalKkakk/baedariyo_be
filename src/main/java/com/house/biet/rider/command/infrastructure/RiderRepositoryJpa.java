package com.house.biet.rider.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.rider.command.domain.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RiderRepositoryJpa
        extends JpaRepository<Rider, Long> {

    @Query("select r.id from Rider r where r.nickname.value = :nickname")
    Optional<Long> findIdByNickname(@Param("nickname") String nickname);
}
