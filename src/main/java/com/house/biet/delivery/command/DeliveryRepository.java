package com.house.biet.delivery.command;

import com.house.biet.delivery.command.domain.aggregate.Delivery;

import java.util.Optional;

public interface DeliveryRepository {

    Delivery save(Delivery delivery);

    Optional<Delivery> findById(Long id);

    Optional<Delivery> findByOrderId(Long orderId);
}