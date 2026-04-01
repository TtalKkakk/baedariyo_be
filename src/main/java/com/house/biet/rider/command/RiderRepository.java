package com.house.biet.rider.command;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.rider.command.domain.entity.Rider;

import java.util.Optional;

/**
 * Rider 도메인의 영속성 처리를 담당하는 Repository 인터페이스
 *
 * Rider 엔티티의 저장, 단건 조회, 존재 여부 확인을 책임 범위로 가짐
 *
 * Rider의 상태 판단 및 비즈니스 규칙은
 * Service 계층 또는 Rider 도메인 엔티티에서 처리함
 */
public interface RiderRepository {

    /**
     * Rider 엔티티 저장
     *
     * @param rider 저장 대상 Rider 엔티티
     * @return 저장된 Rider 엔티티
     */
    Rider save(Rider rider);

    /**
     * Rider ID 기준 Rider 단건 조회
     *
     * @param riderId Rider 식별자
     * @return Rider 존재 시 Optional<Rider>, 미존재 시 Optional.empty()
     */
    Optional<Rider> findById(Long riderId);

    /**
     * Rider ID 기준 존재 여부 확인
     *
     * @param riderId Rider 식별자
     * @return 존재 시 true, 미존재 시 false
     */
    boolean existsById(Long riderId);

    /**
     * 라이더 닉네임으로 라이더 ID를 조회
     *
     * @param nickname 조회할 라이더 닉네임 값
     * @return 라이더 ID를 담은 Optional
     */
    Optional<Long> findRiderIdByNickname(String nickname);

    /**
     * account ID 로 라이더 ID를 조회
     *
     * @param accountId account Id
     * @return 라이더 ID를 담은 Optional
     */
    Optional<Long> findRiderIdByAccountId(Long accountId);
}
