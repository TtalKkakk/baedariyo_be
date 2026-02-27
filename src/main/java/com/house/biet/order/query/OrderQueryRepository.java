package com.house.biet.order.query;

import com.house.biet.global.response.CustomException;

/**
 * Order 조회 전용 Repository Port.
 *
 * <p>
 * Delivery 등 외부 도메인에서
 * 주문 정보를 조회하기 위한 인터페이스이다.
 * </p>
 */
public interface OrderQueryRepository {

    /**
     * 특정 주문에 배정된 배달원의 ID를 조회한다.
     *
     * @param orderId 주문 ID
     * @return 해당 주문의 riderId
     * @throws CustomException 주문이 존재하지 않는 경우
     */
    Long findRiderIdByOrderId(Long orderId);
}