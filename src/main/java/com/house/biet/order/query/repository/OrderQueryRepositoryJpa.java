package com.house.biet.order.query.repository;

import com.house.biet.order.command.domain.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Order 조회용 JPA Repository.
 */
public interface OrderQueryRepositoryJpa
        extends JpaRepository<Order, Long> {

    /**
     * 주문 ID로 배달원 ID를 조회한다.
     *
     * @param orderId 주문 ID
     * @return riderId
     */
    @Query("select o.riderId from Order o where o.id = :orderId")
    Long findRiderIdByOrderId(@Param("orderId") Long orderId);
}