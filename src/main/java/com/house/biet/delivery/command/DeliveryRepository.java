package com.house.biet.delivery.command;

import com.house.biet.delivery.command.domain.aggregate.Delivery;

import java.util.Optional;

public interface DeliveryRepository {

    /**
     * 대상을 저장한다
     *
     * @param delivery delivery 값
     * @return save 결과
     */
    Delivery save(Delivery delivery);

    /**
     * 식별자을 조회한다
     *
     * @param id id 값
     * @return 조회 결과
     */
    Optional<Delivery> findById(Long id);

    /**
     * 주문 식별자을 조회한다
     *
     * @param orderId 주문 식별자
     * @return 조회 결과
     */
    Optional<Delivery> findByOrderId(Long orderId);
}