package com.house.biet.order.command.domain.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderRiderAssignService {

    private final OrderRepository orderRepository;

    /**
     * 주문에 라이더를 배정한다.
     *
     * @param orderId 주문 ID
     * @param riderId 라이더 ID
     * @throws CustomException 주문이 존재하지 않는 경우
     */
    @Transactional
    public void assignRider(Long orderId, Long riderId) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.assignRider(riderId);
    }
}
