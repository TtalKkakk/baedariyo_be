package com.house.biet.delivery.command.repository;

import com.house.biet.delivery.command.DeliveryRepository;
import com.house.biet.delivery.command.domain.aggregate.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryJpaAdapter implements DeliveryRepository {

    private final DeliveryRepositoryJpa deliveryRepositoryJpa;

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryRepositoryJpa.save(delivery);
    }

    @Override
    public Optional<Delivery> findById(Long id) {
        return deliveryRepositoryJpa.findById(id);
    }

    @Override
    public Optional<Delivery> findByOrderId(Long orderId) {
        return deliveryRepositoryJpa.findByOrderId(orderId);
    }
}
