package com.house.biet.order.command.application.port;

import com.house.biet.order.command.domain.event.OrderCreatedEvent;

import java.util.List;

public interface RiderFinder {

    /**
     * 픽업 위치를 기준으로 일정 반경 내에 있는 배달 기사 후보 목록을 조회합니다.
     *
     * @param pickupLocationDto 픽업 위치 정보
     * @param radiusKm 검색 반경 (단위: km)
     * @return 검색된 배달 기사 후보 목록
     */
    List<RiderCandidate> findNearby(
            OrderCreatedEvent.PickupLocationDto pickupLocationDto,
            double radiusKm
    );
}
