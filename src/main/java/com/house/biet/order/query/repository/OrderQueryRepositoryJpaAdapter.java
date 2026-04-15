package com.house.biet.order.query.repository;

import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.order.query.OrderQueryRepository;
import com.house.biet.order.query.repository.dto.OrderSummaryQueryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.house.biet.store.command.domain.aggregate.QStore.store;
import static com.house.biet.order.command.domain.aggregate.QOrder.order;

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
    private final JPAQueryFactory queryFactory;

    @Override
    public Long findRiderIdByOrderId(Long orderId) {
        Long riderId = orderQueryRepositoryJpa.findRiderIdByOrderId(orderId);
        if (riderId == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND_OR_RIDER_NOT_ASSIGNED);
        }

        return riderId;
    }

    @Override
    public List<OrderSummaryQueryDto> findOrderSummariesByOrderStatus(OrderStatus orderStatus) {
        return queryFactory
                .select(Projections.constructor(
                        OrderSummaryQueryDto.class,
                        order.id,
                        store.publicId,
                        store.storeName.value,
                        store.storeGeoLocation.latitude,
                        store.storeGeoLocation.longitude,
                        order.createdAt
                ))
                .from(order)
                .join(store).on(order.storeId.eq(store.id))
                .where(order.status.eq(orderStatus))
                .orderBy(order.createdAt.desc())
                .fetch();
    }
}