package com.house.biet.order.command.application;

import com.house.biet.rider.query.RiderQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRiderFacade {

    private final RiderQueryService riderQueryService;
    private final OrderRiderAssignService orderRiderAssignService;

    /**
     * 라이더을 할당한다
     *
     * @param accountId 계정 식별자
     * @param orderId 주문 식별자
     */
    @Transactional
    public void assignRider(Long accountId, Long orderId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        orderRiderAssignService.assignRider(orderId, riderId);
    }
}
