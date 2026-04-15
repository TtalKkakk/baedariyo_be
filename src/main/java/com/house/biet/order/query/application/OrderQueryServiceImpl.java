package com.house.biet.order.query.application;

import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.order.query.OrderQueryRepository;
import com.house.biet.order.query.repository.dto.OrderSummaryQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * OrderQueryService 구현체.
 */
@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;

    @Override
    public boolean isRiderOfOrder(Long orderId, Long riderId) {
        Long assignedRiderId = orderQueryRepository.findRiderIdByOrderId(orderId);

        return assignedRiderId.equals(riderId);
    }

    @Override
    public List<OrderSummaryQueryDto> findOrderSummariesByOrderStatus(OrderStatus orderStatus) {
        return orderQueryRepository.findOrderSummariesByOrderStatus(orderStatus);
    }
}