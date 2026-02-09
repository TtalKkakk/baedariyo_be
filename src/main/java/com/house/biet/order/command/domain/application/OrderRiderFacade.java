package com.house.biet.order.command.domain.application;

import com.house.biet.rider.query.RiderQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRiderFacade {

    private final RiderQueryService riderQueryService;
    private final OrderRiderAssignService orderRiderAssignService;

    @Transactional
    public void assignRider(Long accountId, Long orderId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        orderRiderAssignService.assignRider(orderId, riderId);
    }
}
