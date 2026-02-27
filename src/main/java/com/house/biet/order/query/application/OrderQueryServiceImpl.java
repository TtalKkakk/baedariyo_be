package com.house.biet.order.query.application;

import com.house.biet.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}