package com.house.biet.order.command.domain.event;

import com.house.biet.order.command.domain.aggregate.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderCreatedEvent {

    private Long orderId;
    private Long storeId;
    private PickupLocationDto pickupLocationDto;
    private LocalDateTime createdAt;

    public static OrderCreatedEvent from(Order order) {
        return new OrderCreatedEvent(
                order.getId(),
                order.getStoreId(),
                new PickupLocationDto(
                        order.getDeliveryLocation().getLatitude(),
                        order.getDeliveryLocation().getLongitude(),
                        order.getDeliveryLocation().getRegion()
                ),
                order.getCreatedAt()
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PickupLocationDto {
        private double latitude;
        private double longitude;
        private String region;
    }
}
