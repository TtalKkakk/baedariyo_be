package com.house.biet.order.query.application;

import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.order.query.application.dto.OrderSummaryResponseDto;

import java.util.List;

/**
 * Order 조회 전용 서비스.
 *
 * <p>
 * 외부 도메인(Delivery 등)에서
 * 주문 조회 관련 비즈니스 로직을 수행한다.
 * </p>
 */
public interface OrderQueryService {

    /**
     * 특정 주문의 배달원인지 여부를 확인한다.
     *
     * @param orderId 주문 ID
     * @param riderId 배달원 ID
     * @return true면 해당 주문의 배달원
     */
    boolean isRiderOfOrder(Long orderId, Long riderId);

    List<OrderSummaryResponseDto> findOrderSummariesByOrderStatus(OrderStatus orderStatus);
}
