package com.house.biet.order.query;

import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.global.response.CustomException;
import com.house.biet.order.query.repository.dto.OrderSummaryQueryDto;

import java.util.List;

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
     */
    Long findRiderIdByOrderId(Long orderId);

    /**
     * orderStatus 에 맞는 Order 정보를 가져온다.
     *
     * @param orderStatus 현재 주문 상태
     * @return 주문 정보 List
     */
    List<OrderSummaryQueryDto> findOrderSummariesByOrderStatus(OrderStatus orderStatus);
}