package com.house.biet.delivery.command.repository;

import com.house.biet.delivery.command.domain.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepositoryJpa
        extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderId(Long orderId);
}