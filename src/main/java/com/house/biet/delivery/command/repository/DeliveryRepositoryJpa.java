package com.house.biet.delivery.command.repository;

import com.house.biet.delivery.command.domain.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepositoryJpa
        extends JpaRepository<Delivery, Long> {

    /**
     * 주문 식별자을 조회한다
     *
     * @param orderId 주문 식별자
     * @return 조회 결과
     */
    Optional<Delivery> findByOrderId(Long orderId);
}