package com.house.biet.order.command;

import com.house.biet.order.command.domain.aggregate.Order;

import java.util.Optional;

public interface OrderRepository {

    /**
     * 주문 저장
     *
     * @param order order 값
     * @return save 결과
     */
    Order save(Order order);

    /**
     * ID로 주문 조회
     *
     * @param orderId 주문 식별자
     * @return 조회 결과
     */
    Optional<Order> findById(Long orderId);

    /**
     * 주문 존재 여부 확인
     *
     * @param orderId 주문 식별자
     * @return existsById 결과
     */
    boolean existsById(Long orderId);

    /**
     * 주문 조회 (상태 변경용, 동시성 제어)
     *
     * @param orderId 주문 식별자
     * @return 조회 결과
     */
    Optional<Order> findByIdForUpdate(Long orderId);
}
