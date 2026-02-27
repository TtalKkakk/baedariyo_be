package com.house.biet.order.query.repository;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * OrderQueryRepository JPA 구현체.
 *
 * <p>
 * Order 조회 Port의 JPA Adapter이다.
 * </p>
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryJpaAdapter implements OrderQueryRepository {

    private final OrderQueryRepositoryJpa orderQueryRepositoryJpa;

    @Override
    public Long findRiderIdByOrderId(Long orderId) {
        Long riderId = orderQueryRepositoryJpa.findRiderIdByOrderId(orderId);
        if (riderId == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND_OR_RIDER_NOT_ASSIGNED);
        }

        return riderId;
    }
}