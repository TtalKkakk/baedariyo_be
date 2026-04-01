package com.house.biet.application.flow;

import com.house.biet.delivery.command.application.DeliveryService;
import com.house.biet.order.command.application.OrderRiderAssignService;
import com.house.biet.order.command.application.OrderService;
import com.house.biet.rider.query.RiderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryOrderFacade {

    private final DeliveryService deliveryService;
    private final OrderService orderService;
    private final RiderQueryService riderQueryService;
    private final OrderRiderAssignService orderRiderAssignService;

    /**
     * 라이더을 할당한다
     *
     * @param orderId 주문 식별자
     * @param accountId 계정 식별자
     */
    public void assignRider(Long orderId, Long accountId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        deliveryService.assignRider(orderId, riderId);
        orderRiderAssignService.assignRider(orderId, riderId);
    }

    /**
     * 배달을 시작한다
     *
     * @param orderId 주문 식별자
     */
    public void startDelivery(Long orderId) {
        deliveryService.inDelivery(orderId);
        orderService.markDelivering(orderId);
    }

    /**
     * 배달을 완료한다
     *
     * @param orderId 주문 식별자
     */
    public void completeDelivery(Long orderId) {
        deliveryService.complete(orderId);
        orderService.markDelivered(orderId);
    }
}
