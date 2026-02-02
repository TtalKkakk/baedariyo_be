package com.house.biet.rider.command;

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
     * 신규 Rider 생성 또는 Rider 상태 변경 이후
     * 영속화 시 사용
     *
     * @param rider 저장 대상 Rider 엔티티
     * @return 저장된 Rider 엔티티
     */
    Rider save(Rider rider);

    /**
     * Rider ID 기준 Rider 단건 조회
     *
     * Rider의 상태 확인, 상태 변경 등
     * 비즈니스 로직 수행을 위한 엔티티 조회 목적
     *
     * @param riderId Rider 식별자
     * @return Rider 존재 시 Optional<Rider>, 미존재 시 Optional.empty()
     */
    Optional<Rider> findById(Long riderId);

    /**
     * Rider ID 기준 존재 여부 확인
     *
     * Rider 엔티티 전체 로딩 없이
     * 단순 존재 여부만 필요할 경우 사용
     *
     * @param riderId Rider 식별자
     * @return 존재 시 true, 미존재 시 false
     */
    boolean existsById(Long riderId);
}
