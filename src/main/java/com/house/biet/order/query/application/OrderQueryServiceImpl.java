package com.house.biet.order.query.application;

import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.global.route.infrastructure.DistanceRouteCalculator;
import com.house.biet.order.query.OrderQueryRepository;
import com.house.biet.order.query.application.dto.OrderSummaryResponseDto;
import com.house.biet.order.query.repository.dto.OrderSummaryQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * OrderQueryService 구현체.
 */
@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderQueryRepository orderQueryRepository;
    private final DistanceRouteCalculator distanceRouteCalculator;

    @Override
    public boolean isRiderOfOrder(Long orderId, Long riderId) {
        Long assignedRiderId = orderQueryRepository.findRiderIdByOrderId(orderId);

        return assignedRiderId.equals(riderId);
    }

    @Override
    public List<OrderSummaryResponseDto> findOrderSummariesByOrderStatus(OrderStatus orderStatus) {
        return orderQueryRepository.findOrderSummariesByOrderStatus(orderStatus).stream()
                .map(this::toResponse)
                .toList();
    }

    private OrderSummaryResponseDto toResponse(OrderSummaryQueryDto queryDto) {
        double distanceKm = calculateDistanceKm(queryDto);
        int estimatedMinutes = distanceRouteCalculator.estimateMinutes(
                queryDto.storeLatitude(),
                queryDto.storeLongitude(),
                queryDto.customerLatitude(),
                queryDto.customerLongitude()
        );

        return new OrderSummaryResponseDto(
                queryDto.orderId(),
                queryDto.storeName(),
                queryDto.storeAddress(),
                queryDto.customerAddress(),
                queryDto.fee(),
                String.format("%.1fkm", distanceKm),
                estimatedMinutes
        );
    }

    private double calculateDistanceKm(OrderSummaryQueryDto queryDto) {
        double lat1 = Math.toRadians(queryDto.storeLatitude());
        double lon1 = Math.toRadians(queryDto.storeLongitude());
        double lat2 = Math.toRadians(queryDto.customerLatitude());
        double lon2 = Math.toRadians(queryDto.customerLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
    }
}
