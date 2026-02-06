package com.house.biet.order.command.infrastructure;

import com.house.biet.order.command.OrderRepository;
import com.house.biet.order.command.domain.aggregate.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryJpaAdapter implements OrderRepository {

    private final OrderRepositoryJpa orderRepositoryJpa;

    @Override
    public Order save(Order order) {
        return orderRepositoryJpa.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepositoryJpa.findById(orderId);
    }

    @Override
    public boolean existsById(Long orderId) {
        return orderRepositoryJpa.existsById(orderId);
    }

    @Override
    public Optional<Order> findByIdForUpdate(Long orderId) {
        return orderRepositoryJpa.findByIdForUpdate(orderId);
    }
}
